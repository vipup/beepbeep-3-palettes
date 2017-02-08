/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.gnuplot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.ArrayDeque;
import java.util.TreeMap;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.sets.Multiset;
import ca.uqac.lif.cep.tuples.Tuple;
import ca.uqac.lif.cep.numbers.NumberCast;

/**
 * Generates a Gnuplot file from a 2D {@link ca.uqac.lif.cep.sets.Multiset}.
 */

public class GnuplotScatterplot extends GnuplotProcessor
{
	/**
	 * The name of the column containing the <i>x</i> values of the
	 * plot
	 */
	protected String m_xHeader;
	
	/**
	 * The title of the "x" axis
	 */
	protected String m_xTitle;
	
	/**
	 * The title of the "y" axis
	 */
	protected String m_yTitle;
	
	/**
	 * The names of the other columns. We store them into an array,
	 * as we cannot guarantee that every line of the input data will
	 * list them in the same order.
	 */
	protected String[] m_otherHeaders;
	
	public GnuplotScatterplot()
	{
		super();
		m_lastPlot = null;
		m_title = "Sample plot generated by BeepBeep 3";
		m_otherHeaders = null;
		m_xTitle = "";
		m_yTitle = "";
	}
	
	/**
	 * Defines the name of the column that contains the "x" values of
	 * the plot
	 * @param name The name of the column
	 * @return The plot
	 */
	public GnuplotScatterplot setX(String name)
	{
		m_xHeader = name;
		return this;
	}
	
	/**
	 * Sets the title of the "x" axis
	 * @param title The title
	 * @return The plot
	 */
	public GnuplotScatterplot setXTitle(String title)
	{
		m_xTitle = title;
		return this;
	}
	
	/**
	 * Sets the title of the "y" axis
	 * @param title The title
	 * @return The plot
	 */
	public GnuplotScatterplot setYTitle(String title)
	{
		m_yTitle = title;
		return this;
	}

	@Override
	protected StringBuilder computePlot(Multiset bag) 
	{
		if (m_otherHeaders == null)
		{
			// We haven't harvested the column names yet: do that first
			getColumnNames(bag);
		}
		return generatePlot(bag);
	}
	
	protected StringBuilder generatePlot(Multiset bag)
	{
		StringBuilder out = new StringBuilder();
		StringBuilder plot_data = generatePlotData(bag);
		out.append("set terminal ").append(m_terminal).append("\n");
		out.append("set title \"").append(m_title).append("\"\n");
		out.append("set xlabel \"").append(m_xTitle).append("\"\n");
		out.append("set ylabel \"").append(m_yTitle).append("\"\n");
		out.append("set datafile separator \",\"\n");
		out.append("plot ");
		for (int i = 0; i < m_otherHeaders.length; i++)
		{
			String header = m_otherHeaders[i];
			if (i > 0)
			{
				out.append(", ");
			}
			out.append("\"-\" u 1:").append(i + 2).append(" t \"").append(header).append("\" w linespoints");
		}
		out.append("\n");
		// Repeat the data as many times as there are columns
		for (int i = 0; i < m_otherHeaders.length; i++)
		{
			out.append(plot_data).append("\ne\n");
		}
		return out;
	}
	
	protected StringBuilder generatePlotData(Multiset bag)
	{
		SortedMap<Float,StringBuilder> values = new TreeMap<Float,StringBuilder>();
		Set<Object> bag_elements = bag.keySet();
		// First, fetch all lines
		for (Object bag_element : bag_elements)
		{
			if (bag_element instanceof Tuple)
			{
				Tuple tuple = (Tuple) bag_element;
				float x_value = NumberCast.getNumber(tuple.get(m_xHeader)).floatValue();
				StringBuilder line = new StringBuilder();
				line.append(x_value);
				for (String key : m_otherHeaders)
				{
					line.append(",").append(tuple.get(key));
				}
				values.put(x_value, line);
			}
		}
		// Then, output these lines, sorted by the value of the "x" column
		StringBuilder plot_contents = new StringBuilder();
		for (StringBuilder sb : values.values())
		{
			plot_contents.append(sb).append("\n");
		}
		return plot_contents;
	}
	
	/**
	 * Fetches column names from an element (i.e. a tuple) from the
	 * bag.
	 * <p><strong>Warning:</strong> Gnuplot can handle a maximum of
	 * <u>7</u> columns, and <em>segfaults</em> when given more. To avoid
	 * that, this method limits itself to the first 7 column names it
	 * encounters and discards the rest.</p> 
	 * @param bag The data bag
	 */
	protected void getColumnNames(Multiset bag)
	{
		Tuple nt = (Tuple) bag.getAnyElement();
		List<String> names = new ArrayList<String>();
		int col_count = 0;
		for (String s : nt.keySet())
		{
			if (s.compareTo(m_xHeader) != 0)
			{
				names.add(s);
				col_count++;
			}
			if (col_count == 7)
			{
				// We reached 7 elements: skip the others
				break;
			}
		}
		m_otherHeaders = new String[names.size()];
		names.toArray(m_otherHeaders);
	}

	public static void build(ArrayDeque<Object> stack) throws ConnectorException 
	{
		Processor p = (Processor) stack.pop();
		stack.pop(); // OF
		stack.pop(); // SCATTERPLOT
		stack.pop(); // GNUPLOT
		stack.pop(); // THE
		GnuplotScatterplot gps = new GnuplotScatterplot();
		Connector.connect(p, gps);
		stack.push(gps);
	}
	
	@Override
	public GnuplotScatterplot clone()
	{
		return new GnuplotScatterplot();
	}


}
