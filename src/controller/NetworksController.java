package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworksController {

	private String os() {
		return System.getProperty("os.name");
	}

	public void ip() throws IOException {
		System.out.println("Network Adapters:\n");
		boolean isWindows = os().contains("Windows");
		Pattern ipv4;
		Pattern netAdap;
		Matcher matcher;
		BufferedReader bReader;
		if (isWindows) {
			bReader = execCommand("ipconfig");
			ipv4 = Pattern.compile("(?<=ipv4.+:\\s)(\\d+\\.\\d+\\.\\d+\\.\\d+)", Pattern.CASE_INSENSITIVE);
			netAdap = Pattern.compile("adap", Pattern.CASE_INSENSITIVE);
		} else {
			bReader = execCommand("ifconfig");
			ipv4 = Pattern.compile("(?<=inet )(\\d+.\\d+.\\d+.\\d+)");
			netAdap = Pattern.compile("\\w+:(?= flags)");
		}
		String line = bReader.readLine();
		StringBuffer output = new StringBuffer();
		String adp = "";
		while (line != null) {
			matcher = netAdap.matcher(line);
			if (matcher.find()) {
				adp = isWindows ? line : matcher.group();
			} else {
				matcher = ipv4.matcher(line);
				if (matcher.find()) {
					output.append(adp);
					output.append("\n\tIPv4: \u001B[33m");
					output.append(matcher.group());
					output.append("\u001B[0m\n\n");
				}
			}
			line = bReader.readLine();
		}

		System.out.println(output.toString());

		bReader.close();

	}

	public void ping(String url) throws IOException {
		System.out.println("Pinging \033[0;33m" + url + "\033[0m...");
		boolean isWindows = os().contains("Windows");
		Pattern average;
		Matcher matcher;
		BufferedReader bReader;
		if (isWindows) {
			average = Pattern.compile("(?<=(M.dia = )|(Average = ))\\d+");
			bReader = execCommand("ping -4 -n 10 " + url);
		} else {
			average = Pattern.compile("(?<=(mdev = [^/]+/))[^/]+(?=/)");
			bReader = execCommand("ping -4 -c 10 " + url);
		}
		String line = bReader.readLine();
		do {
			line = bReader.readLine();
			matcher = average.matcher(line);
		} while (!matcher.find());

		System.out.println("Average: " + matcher.group() + " ms");

		bReader.close();

	}

	private BufferedReader execCommand(String command) throws IOException {
		return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream()));
	}
}
