/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// utgb-shell Project
//
// Deploy.java
// Since: Jan 14, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;
import java.util.Properties;

import org.utgenome.config.UTGBConfig;
import org.xerial.util.FileUtil;
import org.xerial.util.opt.Option;

public class Deploy extends UTGBShellCommand {

	@Option(symbol = "p", longName = "path", varName = "CONTEXT_PATH", description = "web application's context path. e.g., myapp")
	private String contextPath = null;

	@Option(symbol = "n", description = "do not generate context.xml")
	boolean noContextXML = false;

	@Option(symbol = "c", longName = "clean", description = "force recompilation of the project (utgb clean, utgb compile), then deploy")
	boolean forceClean = false;

	@Option(symbol = "m", description = "Tomcat manager url. Default is http://localhost:8080/manager")
	private String tomcatManager = "http://localhost:8080/manager";

	public Deploy() {
	}

	@Override
	public void execute(String[] args) throws Exception {
		if (!isInProjectRoot())
			throw new UTGBShellException("not in the project root");

		// create war/utgb folder
		FileUtil.mkdirs(new File(getProjectRoot(), "war/utgb"));

		if (forceClean) {
			UTGBShell.runCommand(globalOption, "clean");
			UTGBShell.runCommand(globalOption, "compile");
		}

		UTGBConfig config = loadUTGBConfig();

		if (contextPath == null)
			contextPath = config.projectName;
		// generate context.xml file
		if (!noContextXML)
			createContextXML(contextPath, new File("").getAbsolutePath(), false);

		Properties prop = new Properties();
		prop.setProperty("maven.tomcat.url", tomcatManager);
		prop.setProperty("maven.tomcat.path", contextPath.startsWith("/") ? contextPath : "/" + contextPath);
		maven("tomcat:deploy", prop);

	}

	@Override
	public String name() {
		return "deploy";
	}

	@Override
	public String getOneLinerDescription() {
		return "deploy the project (war file) to the remote Tomcat server";
	}

}
