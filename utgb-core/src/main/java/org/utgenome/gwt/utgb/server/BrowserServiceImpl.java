/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// GenomeBrowser Project
//
// BrowserServiceImpl.java
// Since: Apr 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.utgenome.UTGBException;
import org.utgenome.gwt.utgb.client.BrowserService;
import org.utgenome.gwt.utgb.client.bean.DatabaseEntry;
import org.utgenome.gwt.utgb.client.bean.track.TrackDescription;
import org.utgenome.gwt.utgb.client.bio.AlignmentResult;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.ChrRange;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.Locus;
import org.utgenome.gwt.utgb.client.track.bean.SearchResult;
import org.utgenome.gwt.utgb.client.track.bean.TrackBean;
import org.utgenome.gwt.utgb.server.app.ChromosomeMap.Comparator4ChrName;
import org.utgenome.gwt.utgb.server.util.WebApplicationResource;
import org.xerial.core.XerialException;
import org.xerial.db.DBException;
import org.xerial.db.sql.ConnectionPool;
import org.xerial.db.sql.DatabaseAccess;
import org.xerial.db.sql.ResultSetHandler;
import org.xerial.db.sql.SQLExpression;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.db.sql.sqlite.SQLiteCatalog;
import org.xerial.json.JSONArray;
import org.xerial.json.JSONObject;
import org.xerial.util.StopWatch;
import org.xerial.util.bean.BeanHandler;
import org.xerial.util.bean.BeanUtil;
import org.xerial.util.log.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * {"species":{"type":0, "target":"human"}, "genome":{"type":0}, "chromosome":{"type":0}}
 * 
 * 
 * @author leo
 * 
 */
public class BrowserServiceImpl extends RemoteServiceServlet implements BrowserService {

	static Logger _logger = Logger.getLogger(BrowserServiceImpl.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SQLiteAccess _query;

	DatabaseAccess _dbAccess;

	ConnectionPool _connectionPool;

	public BrowserServiceImpl() throws DBException {
		/*
		 * _connectionPool = new ConnectionPoolImpl(SQLite.driverName, SQLite.getDatabaseAddress("mock/tracklist.db"));
		 * _dbAccess = new DatabaseAccess(_connectionPool); _query = new SQLiteAccess(_dbAccess);
		 */
	}

	public String getDataModelParameter(String amoebaQuery) {
		return null;
	}

	public String getHTTPContent(String url) {
		try {

			BufferedReader in = openURL(url);

			StringWriter buffer = new StringWriter();
			BufferedWriter out = new BufferedWriter(buffer);
			String line;
			while ((line = in.readLine()) != null) {
				out.append(line);
				out.newLine();
			}
			in.close();
			out.flush();
			return buffer.toString();
		}
		catch (IOException e) {
			_logger.error(e);
			return "";
		}
	}

	public String getDatabaseCatalog(String jdbcAddress) {
		try {
			// TODO connection pool
			SQLiteAccess dbAccess = new SQLiteAccess(jdbcAddress);
			SQLiteCatalog catalog = dbAccess.getCatalog();
			String json = catalog.toJSON();
			_logger.debug("catalog: " + json);
			return json;
		}
		catch (DBException e) {
			_logger.error(e);
			return "";
		}
	}

	public String getTableData(String jdbcAddress, String tableName) {
		try {
			SQLiteAccess dbAccess = new SQLiteAccess(jdbcAddress);
			String sql = SQLExpression.fillTemplate("select * from $1", tableName);
			List<JSONObject> result = dbAccess.jsonQuery(sql);
			JSONObject resultObj = new JSONObject();
			JSONArray resultArray = new JSONArray();
			for (JSONObject row : result) {
				resultArray.add(row);
			}
			resultObj.put("data", resultArray);

			String json = resultObj.toJSONString();
			_logger.debug("query result: " + json);
			return json;
		}
		catch (DBException e) {
			_logger.error(e);
			return "";
		}
	}

	public List<TrackBean> getTrackList(int entriesPerPage, int page) {
		ArrayList<TrackBean> trackList = new ArrayList<TrackBean>();
		ArrayList<String> trackXMLFileList = WebApplicationResource.find(this.getServletContext(), "/tracks/", "(.*)\\.xml$", true);
		for (String xmlPath : trackXMLFileList) {
			try {
				trackList.add(loadTrackInfo(xmlPath));
			}
			catch (UTGBException e) {
				_logger.error(e);
			}
		}

		return trackList;
	}

	private TrackBean loadTrackInfo(String trackXMLResourcePath) throws UTGBException {
		try {
			BufferedReader xmlReader = WebApplicationResource.openResource(this.getServletContext(), trackXMLResourcePath);
			TrackBean trackInfo = BeanUtil.createXMLBean(TrackBean.class, xmlReader);
			return trackInfo;
		}
		catch (XerialException e) {
			throw new UTGBException(e);
		}
		catch (IOException e) {
			throw new UTGBException(e);
		}
	}

	public List<TrackBean> getTrackList(String prefix, int entriesPerPage, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	public int numHitsOfTracks(String prefix) {
		// TODO Auto-generated method stub
		return 0;
	}

	public SearchResult keywordSearch(String species, String revision, String keyword, int numEntriesPerPage, int page) {

		String keywordSearchURL = "http://utgenome.org/service/utgb-keyword/search";
		keywordSearchURL += String.format("?revision=%s&keyword=%s&page=%d&pagewidth=%d", revision, keyword, page, numEntriesPerPage);
		if (species != null)
			keywordSearchURL += "&species=" + species;

		_logger.debug(keywordSearchURL);

		boolean isScaffoldSearch = keyword.toLowerCase().contains("scaffold");

		try {
			URL url = new URL(keywordSearchURL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			SearchResult searchResult = new SearchResult();
			BeanUtil.populateBeanWithJSON(searchResult, in);

			/*
			// parse the returned keywords
			String keywordLowerCase = keyword.toLowerCase();
			for (Result result : searchResult.getResult()) {
				if (result.getSpecies() == null || result.getRevision() == null || result.getScaffold() == null)
					continue;

				ArrayList<Tag> matchedTagList = new ArrayList<Tag>();
				if (!isScaffoldSearch) {
					for (Tag tag : result.getKeywords()) {
						if (tag.getKey().toLowerCase().contains(keywordLowerCase) || tag.getValue().toLowerCase().contains(keywordLowerCase)) {
							matchedTagList.add(tag);
						}
					}

				}
				result.clearKeyword();
				for (Tag tag : matchedTagList) {
					result.addKeywords(tag);
				}
			}
			 */

			return searchResult;
		}
		catch (MalformedURLException e) {
			_logger.error(e);
		}
		catch (IOException e) {
			_logger.error(e);
		}
		catch (XerialException e) {
			_logger.error(e);
		}

		return null;
	}

	public void saveView(String viewXML) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("view", viewXML);
	}

	public String getCurrentView() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession(true);
		_logger.debug("session ID = " + session.getId());
		String viewXML = (String) session.getAttribute("view");
		if (viewXML == null || (viewXML != null && viewXML.length() == 0)) {
			try {
				String xml = WebApplicationResource.getContent(this.getServletContext(), "/view/standard.xml");
				return xml;
			}
			catch (IOException e) {
				_logger.error(e);
				return null;
			}
		}
		return viewXML;
	}

	public TrackDescription getTrackDescription(String url) {

		try {
			BufferedReader in = openURL(url);
			return BeanUtil.createXMLBean(TrackDescription.class, in);
		}
		catch (IOException e) {
			_logger.error(e);
		}
		catch (XerialException e) {
			_logger.error(e);
		}

		return null;
	}

	private BufferedReader openURL(String url) throws IOException {
		BufferedReader in;
		if (!url.startsWith("http://")) {
			if (!url.startsWith("/"))
				url = "/" + url;
			_logger.debug("proxy request: " + url);
			in = WebApplicationResource.openResource(this.getServletContext(), url);
		}
		else {
			URL address = new URL(url);
			_logger.debug("proxy request: " + url);
			in = new BufferedReader(new InputStreamReader(address.openStream()));
		}
		return in;
	}

	static class BeanRetriever<T> implements BeanHandler<T> {
		private ArrayList<T> geneList = new ArrayList<T>();

		public BeanRetriever() {
		}

		public ArrayList<T> getResult() {
			return geneList;
		}

		public void handle(T bean) throws Exception {
			geneList.add(bean);
		}
	}

	public List<Gene> getGeneList(String serviceURI) {

		StopWatch sw = new StopWatch();
		try {
			BufferedReader reader = openURL(serviceURI);

			BeanRetriever<Gene> geneRetriever = new BeanRetriever<Gene>();
			BeanUtil.loadJSON(reader, Gene.class, geneRetriever);
			return geneRetriever.getResult();
		}
		catch (Exception e) {
			_logger.error(e);
		}
		finally {
			_logger.debug("proxy request: done " + sw.getElapsedTime() + " sec");
		}
		return new ArrayList<Gene>(); // no result
	}

	public AlignmentResult getAlignment(String serviceURI, String target, String sequence) {
		AlignmentResult result = new AlignmentResult();
		try {

			BufferedReader reader = openURL(serviceURI);
			BeanUtil.populateBeanWithJSON(result, reader);
		}
		catch (Exception e) {
			_logger.error(e);
		}
		return result;
	}

	public ChrRange getChrRegion(String species, String revision) {

		final ChrRange chrRanges = new ChrRange();
		chrRanges.ranges = new ArrayList<ChrLoc>();
		chrRanges.maxLength = -1;

		final ArrayList<String> chrNames = new ArrayList<String>();

		_logger.debug(String.format("%s(%s)", species, revision));

		try {

			File cytoBandDb = new File(WebTrackBase.getProjectRootPath(), "db/" + species + "/" + revision + "/cytoBand/cytoBand.db");

			if (cytoBandDb.exists()) {
				SQLiteAccess dbAccess = new SQLiteAccess(cytoBandDb.getAbsolutePath());

				// get chrom name list
				String sql = WebTrackBase.createSQLStatement("select distinct(chrom) from entry");
				if (_logger.isDebugEnabled())
					_logger.debug(sql);

				dbAccess.query(sql, new ResultSetHandler<Object>() {
					@Override
					public Object handle(ResultSet rs) throws SQLException {
						chrNames.add(rs.getString(1));
						return null;
					}
				});

				// make chromosome ranking
				Object[] chrNamesArray = chrNames.toArray();
				Comparator<Object> comparator = new Comparator4ChrName();
				Arrays.sort(chrNamesArray, comparator);

				// get chrLoc information
				for (int i = 0; i < chrNamesArray.length; i++) {
					sql = WebTrackBase.createSQLStatement("select chrom, min(chromStart), max(chromEnd) from entry where chrom='$1'", chrNamesArray[i]);
					if (_logger.isDebugEnabled())
						_logger.debug(sql);

					dbAccess.query(sql, new ResultSetHandler<Object>() {
						@Override
						public Object handle(ResultSet rs) throws SQLException {
							ChrLoc chrLoc = new ChrLoc();
							chrLoc.target = rs.getString(1);
							chrLoc.start = rs.getLong(2);
							chrLoc.end = rs.getLong(3);

							chrRanges.ranges.add(chrLoc);
							if (_logger.isDebugEnabled())
								_logger.debug(String.format("%s:%d-%d", chrLoc.target, chrLoc.start, chrLoc.end));

							chrRanges.maxLength = Math.max(chrRanges.maxLength, chrLoc.end - chrLoc.start);

							BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
							Graphics2D g = (Graphics2D) image.createGraphics();
							Font f = new Font("SansSerif", Font.PLAIN, 10);
							g.setFont(f);
							FontMetrics fontMetrics = g.getFontMetrics();

							chrRanges.chrNameWidth = Math.max(chrRanges.chrNameWidth, fontMetrics.stringWidth(chrLoc.target));

							return null;
						}
					});
				}
				if (_logger.isDebugEnabled())
					_logger.debug(String.format("max length : %d", chrRanges.maxLength));
			}
		}
		catch (Exception e) {
			_logger.error(e);
		}

		return chrRanges;
	}

	public List<Locus> getLocusList(String dbGroup, String dbName, ChrLoc location) {

		String bssFolder = UTGBMaster.getUTGBConfig().getProperty("utgb.db.folder", WebTrackBase.getProjectRootPath() + "/db");
		File dbFile = new File(bssFolder, String.format("%s/%s", dbGroup, dbName));

		if (dbFile.exists()) {
			try {
				SQLiteAccess dbAccess = new SQLiteAccess(dbFile.getAbsolutePath());
				String sql = WebTrackBase
						.createSQLStatement(
								"select rowid, queryID as name, targetStart as start, targetEnd as end, strand from alignment where target = '$1' and start between $2 - $4 and $3 + $4 and (start <= $3 or end >= $2) order by start, end-start desc",
								location.target, location.start, location.end, 1000);
				//_logger.info(sql);

				List<Locus> result = dbAccess.query(sql, Locus.class);
				//_logger.info(Lens.toJSON(result));
				return result;
			}
			catch (Exception e) {
				_logger.error(e);
			}
		}

		return new ArrayList<Locus>();
	}

	public List<String> getChildDBGroups(String parentDBGroup) {

		String dbFolder = UTGBMaster.getUTGBConfig().getProperty("utgb.db.folder", WebTrackBase.getProjectRootPath() + "/db");
		File parentFolder = new File(dbFolder, parentDBGroup);

		ArrayList<String> result = new ArrayList<String>();
		if (parentFolder.exists()) {
			File[] ls = parentFolder.listFiles();
			if (ls != null) {
				for (File each : ls) {
					if (each.isDirectory()) {
						if (each.getName().startsWith("."))
							continue;
						result.add(parentDBGroup + "/" + each.getName());
					}
				}
			}
		}

		return result;
	}

	public List<String> getDBNames(String dbGroup) {

		String dbFolder = UTGBMaster.getUTGBConfig().getProperty("utgb.db.folder", WebTrackBase.getProjectRootPath() + "/db");
		File groupFolder = new File(dbFolder, dbGroup);

		ArrayList<String> result = new ArrayList<String>();
		if (groupFolder.exists()) {
			File[] ls = groupFolder.listFiles();
			if (ls != null) {
				for (File each : ls) {
					if (each.isFile()) {
						if (each.getName().startsWith("."))
							continue;
						result.add(each.getName());
					}
				}
			}
		}

		return result;
	}

	public List<DatabaseEntry> getDBEntry(String dbGroup) {

		String dbFolder = UTGBMaster.getUTGBConfig().getProperty("utgb.db.folder", WebTrackBase.getProjectRootPath() + "/db");
		File groupFolder = new File(dbFolder, dbGroup);

		ArrayList<DatabaseEntry> result = new ArrayList<DatabaseEntry>();
		if (groupFolder.exists()) {
			File[] ls = groupFolder.listFiles();
			if (ls != null) {
				for (File each : ls) {
					if (each.getName().startsWith("."))
						continue;
					if (each.isFile()) {
						result.add(DatabaseEntry.newFile(each.getName()));
					}
					else if (each.isDirectory()) {
						result.add(DatabaseEntry.newFolder(each.getName()));
					}
				}
			}
		}

		return result;
	}

}
