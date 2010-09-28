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
// Sam2Wig.java
// Since: 2010/09/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.utgenome.format.sam.Sam2WigConverter;
import org.utgenome.util.StandardInputStream;
import org.xerial.util.opt.Argument;

public class Sam2Wig extends UTGBShellCommand {

	@Argument(index = 0, name = "input", required = false)
	String input = "-";
	@Argument(index = 1, name = "output", required = false)
	String output = "-";

	@Override
	public void execute(String[] args) throws Exception {

		Sam2WigConverter converter = new Sam2WigConverter();

		InputStream in = "-".equals(input) ? new StandardInputStream() : new BufferedInputStream(new FileInputStream(input));
		Writer out = "-".equals(output) ? new OutputStreamWriter(System.out) : new BufferedWriter(new FileWriter(output));

		try {
			converter.convert(in, out);
		}
		finally {
			in.close();
			out.close();
		}
	}

	@Override
	public String name() {
		return "sam2wig";
	}

	@Override
	public String getOneLinerDescription() {
		return "create a WIG file (coverage depth) from a given SAM/BAM file";
	}

}
