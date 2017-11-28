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
package ca.uqac.lif.cep.tuples;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.sets.Multiset;

/**
 * Breaks a single tuple into multiple tuples, one for each key-value
 * pair of the original tuple.
 * @author Sylvain Hallé
 */
public class Blow extends UnaryFunction<Tuple,Multiset>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5638490095330260150L;
	public static final Blow instance = new Blow();
	
	Blow()
	{
		super(Tuple.class, Multiset.class);
	}

	@Override
	public Multiset getValue(Tuple x)
	{
		Multiset out = new Multiset();
		for (String key : x.keySet())
		{
			Tuple t = new TupleMap();
			t.put(key, x.get(key));
			out.add(t);
		}
		return out;
	}
	
	@Override
	public Blow duplicate()
	{
		return this;
	}
	
	
}
