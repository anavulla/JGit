/**
 * 
 */
package com.anavulla.main;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.anavulla.model.GitConfig;
import com.anavulla.service.GitService;

/**
 * @author Ajay Kumar Navulla
 *
 */
@ComponentScan("com.anavulla")
@SpringBootApplication
public class App {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	@Value("${git.config.repo}")
	private String GIT_REPOSITORY;

	@Value("${git.config.repo.user}")
	private String GIT_REPOSITORY_USER;

	@Value("${git.config.repo.password}")
	private String GIT_REPOSITORY_PASSWORD;

	@Value("${git.config.repo.branch}")
	private String GIT_REPOSITORY_BRANCH;

	private File fileName = Paths.get("file1.txt").toFile();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(App.class, args);

		int exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
			public int getExitCode() {
				return 0;
			}
		});
		System.exit(exitCode);

	}

	public void run(ApplicationArguments args) throws Exception {

		GitConfig gitConfig = new GitConfig(GIT_REPOSITORY, GIT_REPOSITORY_USER, GIT_REPOSITORY_PASSWORD,
				GIT_REPOSITORY_BRANCH);

		LOGGER.info(gitConfig.toString());

		if (!gitConfig.isNull()) {
			GitService.process(fileName, gitConfig);

		} else {
			LOGGER.error("Please provide valid git config... exiting the job...");
			System.exit(1);
		}
	}

}
