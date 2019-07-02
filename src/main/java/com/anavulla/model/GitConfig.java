package com.anavulla.model;

import java.lang.reflect.Field;

/**
 * @author Ajay Kumar Navulla
 *
 */
public class GitConfig {

	@Override
	public String toString() {
		return "GitConfig [configRepo=" + configRepo + ", configRepoUser=" + configRepoUser + ", configRepoPassword="
				+ configRepoPassword + ", configRepoBranch=" + configRepoBranch + "]";
	}

	public GitConfig() {
		super();
	}

	public GitConfig(String configRepo, String configRepoUser, String configRepoPassword, String configRepoBranch) {
		super();
		this.configRepo = configRepo;
		this.configRepoUser = configRepoUser;
		this.configRepoPassword = configRepoPassword;
		this.configRepoBranch = configRepoBranch;
	}

	private String configRepo;
	private String configRepoUser;
	private String configRepoPassword;
	private String configRepoBranch;

	public String getConfigRepo() {
		return configRepo;
	}

	public void setConfigRepo(String configRepo) {
		this.configRepo = configRepo;
	}

	public String getConfigRepoUser() {
		return configRepoUser;
	}

	public void setConfigRepoUser(String configRepoUser) {
		this.configRepoUser = configRepoUser;
	}

	public String getConfigRepoPassword() {
		return configRepoPassword;
	}

	public void setConfigRepoPassword(String configRepoPassword) {
		this.configRepoPassword = configRepoPassword;
	}

	public String getConfigRepoBranch() {
		return configRepoBranch;
	}

	public void setConfigRepoBranch(String configRepoBranch) {
		this.configRepoBranch = configRepoBranch;
	}

	public boolean isNull() {
		Field fields[] = this.getClass().getDeclaredFields();
		for (Field f : fields) {
			try {
				Object value = f.get(this);
				if (value != null) {
					return false;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		return true;

	}
}
