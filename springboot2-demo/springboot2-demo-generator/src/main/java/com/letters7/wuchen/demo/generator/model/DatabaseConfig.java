package com.letters7.wuchen.demo.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * Created by Owen on 5/13/16.
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseConfig {

	/**
	 * The primary key in the sqlite db
	 */
	private Integer id;

	private String dbType;
	/**
	 * The name of the config
	 */
	private String name;

	private String host;

	private String port;

	private String schema;

	private String username;

	private String password;

	private String encoding;

}
