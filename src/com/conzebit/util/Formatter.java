/*
 * This file is part of myPlan.
 *
 * Plan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Plan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with myPlan.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.conzebit.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Formatter {

	// This is not thread safe, but we're using it in a single thread so no problem ;)
	private static SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private static DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
	
	public static String formatDate(Calendar date) {
		return simpleDateFormat.format(date.getTime());
	}

	public static String formatDate(Date date) {
		return simpleDateFormat.format(date);
	}

	public static String formatFullDate(Date date) {
		return simpleDateFormatFull.format(date);
	}

	public static String formatFullDate(Calendar date) {
		return simpleDateFormatFull.format(date.getTime());
	}

	public static String formatDuration(long duration) {
		StringBuffer sb = new StringBuffer();
		long hours = duration / 3600;
		long minutes = duration / 60;
		long seconds = duration % 60;
		if (hours != 0) {
			sb.append(hours < 10 ? "0":"").append(hours).append(":");
		}
		sb.append(minutes < 10 ? "0":"").append(minutes).append(":");
		sb.append(seconds < 10 ? "0":"").append(seconds);
		return sb.toString();
	}
	
	public static String formatDurationAsString(long duration) {
		long seconds = duration % 60;
		long minutes = duration / 60;
		long hours = 0;
		long days = 0;
		if (minutes >= 60) {
			hours = (long) minutes / 60;
			minutes = minutes % 60;
			if (hours >= 24) {
				days = (long) hours / 24;
				hours = hours % 24;
			}
		}
		StringBuffer sb = new StringBuffer();
		if (days != 0) {
			sb.append(days).append('d');
		}
		if (hours != 0) {
			sb.append(hours).append('h');
		}
		if (minutes != 0) {
			sb.append(minutes).append('m');
		}
    	sb.append(seconds).append('s');
		return sb.toString();
	}

	public static String formatDecimal(double value) {
		return decimalFormat.format(value);
	}
}
