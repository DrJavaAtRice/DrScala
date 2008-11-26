/*BEGIN_COPYRIGHT_BLOCK*

PLT Utilities BSD License

Copyright (c) 2007-2008 JavaPLT group at Rice University
All rights reserved.

Developed by:   Java Programming Languages Team
                Rice University
                http://www.cs.rice.edu/~javaplt/

Redistribution and use in source and binary forms, with or without modification, are permitted 
provided that the following conditions are met:

    - Redistributions of source code must retain the above copyright notice, this list of conditions 
      and the following disclaimer.
    - Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials provided 
      with the distribution.
    - Neither the name of the JavaPLT group, Rice University, nor the names of the library's 
      contributors may be used to endorse or promote products derived from this software without 
      specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*END_COPYRIGHT_BLOCK*/

package edu.rice.cs.plt.collect;

import edu.rice.cs.plt.tuple.Pair;

/**
 * An injective relation: each second (of type {@code T2}) corresponds to at most one first
 * (of type {@code T1}).  This can be viewed as modeling both a one-to-many relationship between
 * firsts and seconds and a function from seconds to firsts.
 */
public interface InjectiveRelation<T1, T2> extends Relation<T1, T2> {

  /** Produce the first corresponding to {@code second}, or {@code null} if there is none. */
  public T1 antecedent(T2 second);
  /**
   * A map view of the relation, mapping seconds to firsts.  Need not allow mutation, but must
   * reflect subsequent changes.
   */
  public LambdaMap<T2, T1> injectionMap();

  /**
   * Add a pair to the set.  If the pair violates the cardinality constraint, throw an exception.
   * @throws IllegalArgumentException  If {@code containsSecond(pair.second())} but not {@code contains(pair)}.
   */
  public boolean add(Pair<T1, T2> pair);
  /**
   * Add {@code Pair.make(first, second)} to the set.  If the pair violates the cardinality constraint,
   * throw an exception.
   * @throws IllegalArgumentException  If {@code containsSecond(second)} but not {@code contains(first, second)}.
   */
  public boolean add(T1 first, T2 second);

  /**
   * Produce the inverse of the relation, derived by swapping the elements of each pair.  Note that the
   * inverse is functional.  Need not allow mutation, but must reflect subsequent changes.
   */
  public FunctionalRelation<T2, T1> inverse();
  
  /**
   * The set of firsts corresponding to a specific second.  Guaranteed to have size 0 or 1.
   * Need not allow mutation, but must reflect subsequent changes.
   */
  public PredicateSet<T1> matchSecond(T2 second);
  
}
