from flask import Flask, jsonify
import pandas as pd
from statsmodels.tsa.arima.model import ARIMA
import matplotlib.pyplot as plt
import seaborn as sns
import requests
import psycopg2
from psycopg2 import sql
from sqlalchemy import create_engine, MetaData, Table, select

app = Flask(__name__)
csv_file = './updated_test data.csv'  # Update with your CSV file path
api_url = "http://localhost:8080/auth/fetch-redis"  # Replace with your API endpoint


response = requests.get(api_url)

json_data=response.json()
df1=pd.DataFrame(json_data)
df1.to_csv(csv_file, mode='a', index=False, header=False)

try:
    df2 = pd.read_csv(csv_file)
    reqKey = df2.loc[0, 'Key']  # Corrected the indexing syntax
    print(f"Value of 'Key' in the second row: {reqKey}")
except FileNotFoundError:
    print("Error: The specified CSV file was not found.")
except KeyError:
    print("Error: The column 'Key' does not exist in the DataFrame.")
except Exception as e:
    print(f"An unexpected error occurred: {e}")
finally:
    print("Execution completed.")

# PostgreSQL connection parameters
db_params = {
    'dbname': 'Predictive_Analysis_Tool',
    'user': 'postgres',
    'password': 'Agartala',
    'host': 'localhost',
    'port': '5432'
}

def create_connection(user_name, password, host_name, port, db_name):
    engine = create_engine(f'postgresql+psycopg2://{user_name}:{password}@{host_name}:{port}/{db_name}')
    return engine

def load_data(filename):
    try:
        df = pd.read_csv(filename)
        return df
    except Exception as e:
        print(f"Error loading CSV file: {e}")
        return None

def preprocess_data(df):
    try:
        # Parse dates with format '%d-%m-%Y'
        df['Demand_date'] = pd.to_datetime(df['Demand_date'], format='%Y-%m-%d')
    except Exception as e:
        print(f"Error parsing dates: {e}")
        return None
    df.set_index('Demand_date', inplace=True)
    df.sort_index(inplace=True)
    return df

def build_arima_model(series, order=(5, 1, 0)):
    model = ARIMA(series, order=order)
    model_fit = model.fit()
    return model_fit

def forecast_sales(model, steps):
    forecast = model.forecast(steps=steps)
    return forecast

def save_forecast_to_db(forecast, start_date):
    try:
        conn = psycopg2.connect(**db_params)
        cur = conn.cursor()
        insert_query = sql.SQL("""
            INSERT INTO forecast_results (forecast_date, forecasted_qty,reqKey)
            VALUES (%s, %s, %s)
        """)
        for i, qty in enumerate(forecast):
            forecast_date = start_date + pd.DateOffset(months=i)
            cur.execute(insert_query, (forecast_date, qty,reqKey))
        conn.commit()
        cur.close()
        conn.close()
    except Exception as e:
        print(f"Error saving to database: {e}")



def fetch_data_from_database(engine, schema_name, table_name):
    try:
        metadata = MetaData()
        metadata.reflect(bind=engine, schema=schema_name, only=[table_name])  # Reflect table structure
        table = metadata.tables[f'{schema_name}.{table_name}']
        stmt = select(table)  # Construct select statement
        with engine.connect() as connection:
            result = connection.execute(stmt)
            data = result.fetchall()  # Fetch all rows from the result
        return data
    except Exception as e:
        print(f'Error fetching data from database: {e}')
        return None

@app.route('/view_redis', methods=['GET'])
def get_redis_data():
    try:
        return jsonify(json_data)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/get_data/<reqKey>', methods=['GET'])
def get_data(reqKey):
    try:
        # Replace with your actual database credentials
        db_username = 'postgres'
        db_password = 'Agartala'
        db_hostname = 'localhost'
        port = 5432
        db_name = 'Predictive_Analysis_Tool'
        schema_name = 'public'
        table_name = 'forecast_results'

        engine = create_connection(db_username, db_password, db_hostname, port, db_name)
        if engine:
            data = fetch_data_from_database(engine, schema_name, table_name)
            if data:
                # Prepare data in JSON format
                formatted_data = [{'date': row[1], 'value': str(row[2])} for row in data]
                return jsonify(formatted_data)
            else:
                return jsonify({'error': 'Failed to fetch data'}), 500
        else:
            return jsonify({'error': 'Database connection failed'}), 500
    
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/api/forecast/<reqKey>', methods=['GET'])
def get_forecast(reqKey):
    try:
        df = load_data(csv_file)
        if df is None:
            return jsonify({'error': 'Failed to load data'}), 500
        
        df = preprocess_data(df)
        if df is None:
            return jsonify({'error': 'Failed to preprocess data'}), 500
        
        series = df['Demand_qty']
        
        model = build_arima_model(series)
        
        # Forecast 12 steps ahead (adjust as needed)
        forecast_steps = 12
        forecast = forecast_sales(model, steps=forecast_steps)
        
        # Save forecast to database
        save_forecast_to_db(forecast, series.index[-1])
        
        # Plotting the forecast
        sns.set(style="whitegrid")
        plt.figure(figsize=(14, 7))
        plt.plot(series.index, series.values, label='Demand Qty', color='blue', linestyle='-', linewidth=2)
        plt.plot(pd.date_range(start=series.index[-1], periods=forecast_steps, freq='M'), forecast, label='Forecasted Qty', color='red', linestyle='--', linewidth=2)
        plt.title('Forecasted Qty', fontsize=16)
        plt.xlabel('Date', fontsize=14)
        plt.ylabel('Demand_qty', fontsize=14)
        plt.legend(fontsize=12)
        plt.tight_layout()
        
        # Show plot in a window
        plt.show()
        
        # Convert forecast to list for JSON serialization
        forecast_data = {
            'forecasted_sales': forecast.tolist()
        }
        
        return jsonify(forecast_data)
    except Exception as e:
        return jsonify({'error': f"An error occurred: {e}"}), 500

@app.route('/', methods=['GET'])
def index():
    return f'''
    <strong> Welcome to my Flask API! Navigate to.... </strong> <br>
    <table border="1">
        <tr>
            <th>SlNo.</th>
            <th>URL</th><th>Purpose</th>
        </tr>
        <tr>
            <td>1</td>
            <td><a href='http://127.0.0.1:5000/view_redis' target='1'>http://127.0.0.1:5000/view_redis</a> </td>
            <td>VIEW Saved Redis Data</td>
        </tr>
        <tr>
            <td>2</td>
            <td><a href='http://127.0.0.1:5000/api/forecast/{reqKey}' target='2'>http://127.0.0.1:5000/api/forecast/{reqKey}</a> </td>
            <td>Saved into Db and view the Forecasted Data</td>
        </tr>
        <tr>
            <td>3</td>
            <td><a href='http://127.0.0.1:5000/get_data/{reqKey}' target='3'>http://127.0.0.1:5000/get_data/{reqKey}</a> </td>
            <td>View the Forecasted records from db with the Requested Key </td>
        </tr>
   </table>
    '''

if __name__ == '__main__':
    app.run(debug=True)
