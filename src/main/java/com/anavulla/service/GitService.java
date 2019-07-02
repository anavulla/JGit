package com.anavulla.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anavulla.model.GitConfig;
import com.anavulla.service.GitService;

/**
 * @author Ajay Kumar Navulla
 * 
 *  Create git config with turning off ssl verify. Git Pull and checkout
 *         particular git branch. Verify if file is different from repo then
 *         push.
 *
 */
public class GitService {

	private static final Logger LOG = LoggerFactory.getLogger(GitService.class);

	public static void process(File fileName, GitConfig gitConfig) throws Exception {
		Git git = null;
		File gitDir = null;

		try {

			Path currentDirPath = fileName.getParentFile().toPath();

			gitDir = new File(currentDirPath.toString(), "git-repo");

			FileUtils.forceMkdir(gitDir);
			FileUtils.cleanDirectory(gitDir);

			LOG.info("==GIT repo is being pulled with required configs==");
			git = Git.init().setDirectory(gitDir).setBare(false).call();
			StoredConfig config = git.getRepository().getConfig();
			config.setString("remote", "origin", "url", gitConfig.getConfigRepo());
			config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
			config.setString("branch", gitConfig.getConfigRepoBranch(), "remote", "origin");
			config.setString("branch", gitConfig.getConfigRepoBranch(), "merge",
					"refs/heads/" + gitConfig.getConfigRepoBranch());
			config.setBoolean("http", null, "sslVerify", false);
			config.save();

			// GIT pull
			PullCommand pull = git.pull();
			pull.setRemote("origin");
			pull.setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitConfig.getConfigRepoUser(),
					gitConfig.getConfigRepoPassword()));
			pull.call();

			// GIT checkout config branch
			git.branchCreate().setForce(true).setName(gitConfig.getConfigRepoBranch())
					.setStartPoint("origin/" + gitConfig.getConfigRepoBranch()).call();
			git.checkout().setName(gitConfig.getConfigRepoBranch()).call();

			LOG.info("==GIT repo has been successfully pulled and tracking on " + gitConfig.getConfigRepoBranch()
					+ "==");

			File fileDir = Paths.get(currentDirPath.toString(), "git-repo", "folder1", "folder2").toFile();

			Path file1 = Paths.get(fileDir.toPath().toString(), "file1.txt");

			boolean isFileNotChanged = FileUtils.contentEquals(fileName, file1.toFile());

			if (!isFileNotChanged) {
				LOG.info("The  file is different from git repo's  file... pushing to git-repo....");
				FileUtils.copyFileToDirectory(fileName, fileDir, true);

				// GIT add
				AddCommand addCommand = git.add();
				addCommand.addFilepattern(".").call();

				// GIT commit
				git.commit().setAuthor("git-service", "git-service@anavulla.com").setMessage("Git Service Processor")
						.call();

				// GIT push
				PushCommand pushCommand = git.push();
				pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
						gitConfig.getConfigRepoUser(), gitConfig.getConfigRepoPassword()));
				pushCommand.call();

				LOG.info("The  file has been successfully pushed to git-repo.");

			} else {
				LOG.info("The efile extracted is same as git repo's  file... not pushing....");
			}

		} catch (Exception e) {
			LOG.error("Exception in GitService process e=" + e);
			System.exit(1);
		} finally {
			git.close();
			FileUtils.forceDelete(gitDir);
		}
	}

}
