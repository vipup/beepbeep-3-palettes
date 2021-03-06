/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2017 Sylvain Hallé

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
package ca.uqac.lif.cep.tuples;

import java.util.ArrayDeque;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.input.TokenFeeder;

/**
 * Creates a feed of events from CRLF-separated string chunks.
 * Note that the input feed must have a trailing CRLF for all elements,
 * including the last. 
 * @author sylvain
 *
 */
public class TupleFeeder extends TokenFeeder
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6864690824383779454L;
	protected FixedTupleBuilder m_builder;
	
	public TupleFeeder()
	{
		this(null);
	}
	
	public TupleFeeder(FixedTupleBuilder builder)
	{
		super();
		m_separatorBegin = "";
		m_separatorEnd = System.getProperty("line.separator");
		m_builder = builder;
	}
	
	@Override
	protected Object createTokenFromInput(String token)
	{
		token = token.trim();
		if (token.isEmpty() || token.startsWith("#"))
		{
			// Ignore comment and empty lines
			return new TokenFeeder.NoToken();
		}
		String[] parts = token.split(",");
		if (m_builder == null)
		{
			// This is the first token we read; it contains the names
			// of the arguments
			m_builder = new FixedTupleBuilder(parts);
			return new TokenFeeder.NoToken();
		}
		return m_builder.createTupleFromString(parts);
	}

	public static void build(ArrayDeque<Object> stack) 
	{
		Object o;
		Processor p; 
		o = stack.pop(); // ) ?
		if (o instanceof String)
		{
			p = (Processor) stack.pop();
			stack.pop(); // (
		}
		else
		{
			p = (Processor) o;
		}
		stack.pop(); // OF
		stack.pop(); // TUPLES
		stack.pop(); // THE
		TupleFeeder tp = new TupleFeeder();
		Connector.connect(p, tp);
		stack.push(tp);
	}
	
	@Override
	public TupleFeeder duplicate()
	{
		return new TupleFeeder();
	}

}