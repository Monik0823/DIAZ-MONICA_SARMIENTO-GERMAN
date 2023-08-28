package com.backend.digitalhouse.integrador;

import com.backend.digitalhouse.integrador.dao.H2Connection;

import java.sql.SQLException;

import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IntegradorApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(IntegradorApplication.class);

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		SpringApplication.run(IntegradorApplication.class, args);
		H2Connection.create();
		LOGGER.info("ClinicaOdontologica is now running...");
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
