package com.central.base.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 公共的出参序列化转化配置
 * @author bin
 *
 * @desc: 针对id生成器long型的id 返回给前端 不能接收long，超过js范围，转换为字符串
 */
@Configuration
public class SerializerConfiguration {

    /**
     * 方式一：此方式可以灵活配置任意类型的序列化反序列化
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer builderCustomizer() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeSerializeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTimeDeserializeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return builder -> {
            // 所有Long类型转换成String到前台
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(dateFormatter));
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeSerializeFormatter));
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeDeserializeFormatter));
        };
    }

    /**
     *
     * 方式二：采用objectMapper注入
     */
//    @Bean
//    public ObjectMapper objectMapper (Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        SimpleModule simpleModule = new SimpleModule();
//        // 直接将所有的Long类型转换为String
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//        return objectMapper;
//    }

}
