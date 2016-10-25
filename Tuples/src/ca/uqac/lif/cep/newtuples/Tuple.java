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
package ca.uqac.lif.cep.newtuples;

import java.util.Map;

/**
 * A (named) tuple is a map between attribute names (character strings) and
 * attribute values (any kind of Object). This class is a generalization of
 * the classical concept of "tuple" in relational databases (where attributes
 * can only have <em>scalar</em> values).
 * <p>
 * There are two direct descendents of the Tuple class:
 * <ul>
 * <li>{@link TupleMap}: the most general implementation of a tuple; this is
 * basically a <code>Map</code> object with a few extra methods</li>
 * <li>{@link TupleFixed}: a fixed tuple assumes stronger hypotheses on its
 * use in order to reduce memory usage and decrease response time.</li>
 * </ul>
 * @author Sylvain Hallé
 *
 */
public abstract class Tuple implements Map<String,Object>
{

}