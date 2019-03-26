package org.scapesoft.networking.management.query.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import org.scapesoft.networking.management.query.ServerQuery;
import org.scapesoft.networking.management.query.ServerQueryHandler;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 22, 2014
 */
public class CommandsQuery extends ServerQuery {

	@Override
	public String[] getQueryListeners() {
		return new String[] { "commands" };
	}

	@Override
	public String getDescription() {
		return "List all commands";
	}

	@Override
	public String onRequest() {
		String string = new String();
		Iterator<Entry<String, ServerQuery>> it$ = ServerQueryHandler.getServerQueries().entrySet().iterator();
		while(it$.hasNext()) {
			Entry<String, ServerQuery> entry = it$.next();
			ServerQuery query = entry.getValue();
			String string1 = new String();
			for (int i = 0; i <query.getQueryListeners().length; i++) {
				String cmd = query.getQueryListeners()[i];
				string1 += cmd + "" + (i == query.getQueryListeners().length - 1 ? "" : ", ");
			}
			String result = string1 + ":\t" + query.getDescription();
			string += result + "\n\t";
		}
		return string;
	}

}
