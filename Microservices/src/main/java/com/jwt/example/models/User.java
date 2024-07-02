package com.jwt.example.models;
import lombok.Data;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("KEY")
public class User implements Serializable{
	private String key=UUID.randomUUID().toString() + "27201" + ((int) (Math.random() * 900) + 100);

    private String category;

    private String sub_category;

    private String expected_period_in_month;

    private String out_data_pattern;

    private String demand_date;
 
    private String demand_qty;

    private String analysis_year; 
}
