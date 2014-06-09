package jsp.controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.HashMap;

import jsp.model.Film;

public class NetworkScanner {
	private static Calendar lastref = null;
	public static HashMap<String, Film> films = new HashMap<>();
	public static HashMap<String, Film> tempFilms = new HashMap<>();

	public NetworkScanner() {

	}

	private static void refreshData() throws IOException {
		films = new HashMap<String, Film>();
		InetAddress localhost = InetAddress.getLocalHost();
		final byte[] ip = localhost.getAddress();

		Thread[] ts = new Thread[254];
		for (int j = 8; j <= 14; j++) {
			final int fooj = j;
			for (int i = 1; i <= 254; i++) {
				final int foo = i;
				ts[i - 1] = new Thread(new Runnable() {
					public void run() {
						ip[2] = (byte) fooj;
						ip[3] = (byte) foo;
						InetAddress address;
						try {
							address = InetAddress.getByAddress(ip);
							if (address.isReachable(10000)) {
								File f = new File("\\\\"
										+ address.getHostAddress() + "\\filmek");
								for (String s : f.list()) {
									String link = "file:/" + address
											+ "/filmek/" + s;
									films.put(link, new Film(s, link));
									System.out.println(link);
								}
							}
						} catch (Exception e) {
						}
					}
				});
				ts[i - 1].start();
			}
		}

	}

	public static String getResult(String filter) {
		String ret = "";

		if (films.size() > 0) {
			for (Film f : films.values()) {
				if (f.getLink().contains(filter))
					ret += f.getLink() + "<br>";
			}
		} else {
			for (Film f : tempFilms.values()) {
				if (f.getLink().contains(filter))
					ret += f.getLink() + "<br>";
			}
		}
		return ret;
	}



	public static void refresh(Calendar now) {
		now.add(Calendar.MINUTE, -1);
		if (lastref == null || lastref.getTime().before(now.getTime()))
			try {
				now.add(Calendar.MINUTE, 1);
				lastref = now;
				System.out.println("REFRESH");
				tempFilms = films;
				refreshData();
			} catch (IOException e) {

			}
	}
}
