/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// utgb-core Project
//
// SAMRead.java
// Since: 2010/03/15
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import org.utgenome.gwt.utgb.client.util.Properties;

/**
 * Genome Read data
 * 
 * @author yoshimura
 * 
 */
public class SAMRead extends SAMReadLight {
	private static final long serialVersionUID = 1L;

	//schema record(qname, flag, rname, start, end, mapq, cigar, mrnm, mpos, isize, seq, qual, tag*)
	public String rname;
	public int mapq; // Mapping Quality
	public String mrnm; // mate reference name
	public int mStart; // mate start (new parameter!) 
	public String seq;
	public String qual;
	public Properties tag;

	public String refSeq;

	public SAMRead() {
		super();
	}

	public SAMRead(int start, int end) {
		super(start, end);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("name=" + qname);
		sb.append(", flag=" + flag);
		sb.append(", start=" + start);
		sb.append(", mapq=" + mapq);
		sb.append(", cigar=" + cigar);
		sb.append(", iSize=" + iSize);
		sb.append(", tag=" + tag);

		return sb.toString();
	}

	public boolean isMate(SAMRead other) {
		if (this.isFirstRead()) {
			if (other.isSecondRead())
				return qname != null && qname.equals(other.qname);
		}
		else {
			if (other.isFirstRead())
				return qname != null && qname.equals(other.qname);
		}
		return false;
	}

	@Override
	public String getSequence() {
		return seq;
	}

	@Override
	public String getQV() {
		return qual;
	}

	@Override
	public String getName() {
		return qname;
	}

	@Override
	public boolean isSense() {
		return (flag & SAMReadFlag.FLAG_STRAND_OF_QUERY) == 0;
	}

	@Override
	public boolean isAntiSense() {
		return !isSense();
	}

	@Override
	public void accept(GenomeRangeVisitor visitor) {
		visitor.visitSAMRead(this);
	}

	public boolean mateIsMappedToTheSameChr() {
		return rname != null && rname.equals(mrnm);
	}

}
