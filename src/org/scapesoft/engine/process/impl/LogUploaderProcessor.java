package org.scapesoft.engine.process.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.scapesoft.Constants;
import org.scapesoft.engine.process.TimedProcess;
import org.scapesoft.utilities.console.logging.FileLogger;
import org.scapesoft.utilities.misc.Config;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 31, 2014
 */
public class LogUploaderProcessor implements TimedProcess {

	public static void main(String[] args) {
		Config.get().load();
		LogUploaderProcessor l = new LogUploaderProcessor();
		l.execute();
	}

	@Override
	public Timer getTimer() {
		return new Timer(1, TimeUnit.MINUTES);
	}

	@Override
	public void execute() {
		if (!Constants.isVPS) {
			return;
		}
		try {
			long timeFromFile = getTimeFromFile();
			/**
			 * Checking to see if there should be a backup generated
			 */
			if (timeFromFile == -1 || TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - getTimeFromFile()) >= 24) {
				startArchiveProcess();
				PrintWriter writer = new PrintWriter(DURATION_FILE);
				writer.print(System.currentTimeMillis());
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startArchiveProcess() throws IOException {
		long start = System.currentTimeMillis();
		File archived = archiveDirectory();
		File logsDirectory = new File(FileLogger.getFileLogger().getLocation());
		if (!archived.getParentFile().exists())
			archived.getParentFile().mkdirs();
		zipDirectory(logsDirectory, archived);
		System.out.println("Archived logs in " + (System.currentTimeMillis() - start) + " ms.");
	}

	/**
	 * Gets the long time from the file
	 *
	 * @return A {@code Long} {@code Object}
	 */
	private long getTimeFromFile() {
		long time = -1;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(DURATION_FILE));
			String line;
			while ((line = reader.readLine()) != null) {
				time = Long.parseLong(line);
				break;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * Archives all of the data in the {@link #FILE_LOCATION} folder.
	 */
	private File archiveDirectory() {
		File file = new File(FILE_LOCATION + "uploads/" + getFileTitle() + ".zip");
		return file;
	}

	/**
	 * Zips the directory to a .zip file
	 *
	 * @param f
	 *            The folder to zip
	 * @param zf
	 *            The zip file
	 * @throws IOException
	 *             Exception thrown
	 */
	private void zipDirectory(File f, File zf) throws IOException {
		ZipOutputStream z = new ZipOutputStream(new FileOutputStream(zf));
		zip(f, f, z);
		z.close();
	}

	/**
	 * Zips a directory into an archive
	 *
	 * @param directory
	 *            The directory to zip to
	 * @param base
	 *            The base folder
	 * @param zos
	 *            The outputstream
	 * @throws IOException
	 *             Exception thrown
	 */
	private void zip(File directory, File base, ZipOutputStream zos) throws IOException {
		File[] files = directory.listFiles();
		files = formatFilesDirectories(files);
		byte[] buffer = new byte[8192];
		int read = 0;
		for (File file2 : files) {
			if (file2.isDirectory()) {
				zip(file2, base, zos);
			} else {
				FileInputStream in = new FileInputStream(file2);
				ZipEntry entry = new ZipEntry(file2.getPath().substring(base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}

	private File[] formatFilesDirectories(File[] files) {
		List<File> fileList = new ArrayList<File>();
		for (File file : files) {
			fileList.add(file);
		}
		ListIterator<File> it$ = fileList.listIterator();
		while (it$.hasNext()) {
			File file = it$.next();
			if (file.isDirectory() && file.getName().contains("backup")) {
				it$.remove();
			}
			if (file.isDirectory() && isOldMonth(file.getName())) {
				it$.remove();
			}
		}
		files = fileList.toArray(new File[fileList.size()]);
		return files;
	}

	private boolean isOldMonth(String directoryName) {
		Calendar cal = Calendar.getInstance();
		Month[] monthNames = Month.values();
		Month currentMonth = monthNames[cal.get(Calendar.MONTH)];

		for (Month month : Month.values()) {
			if (directoryName.toLowerCase().contains(month.name().toLowerCase())) {
				if (month == currentMonth) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	static {
		Config.get().load();
	}

	/**
	 * Creating the title of the file
	 * 
	 * @return A {@code String} {@code Object}
	 */
	private String getFileTitle() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * The location the logs will be uploaded to
	 */
	private static final String FILE_LOCATION = System.getProperty("user.home").contains("admin") ? "C:/PROGRA~2/EASYPH~1.1VC/data/localweb/logs/" : "";
	public static final File DURATION_FILE = new File(FileLogger.getFileLogger().getLocation() + "/log_duration.txt");
}
