/*BEGIN_COPYRIGHT_BLOCK
 *
 * Copyright (c) 2001-2008, JavaPLT group at Rice University (drjava@rice.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the names of DrJava, the JavaPLT group, Rice University, nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software is Open Source Initiative approved Open Source Software.
 * Open Source Initative Approved is a trademark of the Open Source Initiative.
 * 
 * This file is part of DrJava.  Download the current version of this project
 * from http://www.drjava.org/ or http://sourceforge.net/projects/drjava/
 * 
 * END_COPYRIGHT_BLOCK*/

package edu.rice.cs.drjava.model;

import edu.rice.cs.util.UnexpectedException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import java.io.File;

/** Class for a simple document region. If a document is provided, then the region will move within the document.
  * @version $Id$
  */
public class SimpleDocumentRegion implements EnhancedDocumentRegion {
  protected final OpenDefinitionsDocument _doc;
  protected final File _file;
  protected volatile Position _startPosition = null; 
  protected volatile Position _endPosition = null;
  
  /** Create a new simple document region using offsets.
    * @param doc document that contains this region, or null if we don't have a document yet
    * @param file file that contains the region
    * @param so start offset of the region; if doc is non-null, then a Position will be created that moves within the document
    * @param eo end offset of the region; if doc is non-null, then a Position will be created that moves within the document
    */
  public SimpleDocumentRegion(OpenDefinitionsDocument doc, int so, int eo) {
    this(doc, createPosition(doc, so), createPosition(doc, eo));
  }
 
  /** Create a new simple document region with a bona fide document */
  public SimpleDocumentRegion(OpenDefinitionsDocument doc, Position sp, Position ep) {
    assert doc != null;
    _doc = doc;
    _file = doc.getRawFile();  // don't check the validity of _file here
    _startPosition = sp;
    _endPosition = ep;
//    _startOffset = sp.getOffset();
//    _endOffset = ep.getOffset();
  }
  
  private static Position createPosition(OpenDefinitionsDocument doc, int i) {
    try { return doc.createPosition(i); }
    catch(BadLocationException e) { throw new UnexpectedException(e); }
  }

//  /** Structural equality method that copes with null!  This method should be a member of class Object. */
//  public static boolean equals(Object o1, Object o2) { 
//    if (o1 == null) return o2 == null;
//    return o1.equals(o2);
//  }
  
  /** Defines the equality relation on DocumentRegions.  This equivalence relation on allocated objects is finer
    * grained than the equivalence relation induced by compareTo because it requires equality on Position objects, 
    * not just equality of the current offsets of Positions. 
    */
  public final boolean equals(Object o) {
    if (o == null || ! (o instanceof SimpleDocumentRegion)) return false;
    SimpleDocumentRegion r = (SimpleDocumentRegion) o;
    return _doc == r._doc & getStartOffset() == r.getStartOffset() && getEndOffset() == r.getEndOffset();
  }
  
  private int docHashCode() {
    if (_doc == null) return 0;
    return _doc.hashCode();
  }
      
  /** This hash function is consistent with equality. */
  public int hashCode() { return docHashCode() ^ getStartOffset() ^ getEndOffset(); }
  
  /** @return the document, or null if it hasn't been established yet */
  public OpenDefinitionsDocument getDocument() { return _doc; }

  /** @return the file */
  public File getFile() { return _file; }

  /** @return the start offset */
  public int getStartOffset() {
//    if (_startPosition != null) {
//      // if we have a position that moves within the document, update the offset
    int _startOffset = _startPosition.getOffset();
//    }
    return _startOffset;
  }

  /** @return the end offset */
  public int getEndOffset() {
//    if (_endPosition != null) {
//      // if we have a position that moves within the document, update the offset
    int _endOffset = _endPosition.getOffset();
//    }
    return _endOffset;
  }
  
  /** @return the start position */
  public Position getStartPosition() { return _startPosition; }

  /** @return the end offset */
  public Position getEndPosition() { return _endPosition; }
  public String toString() {
    return (/* _doc != null ? */ _doc.toString() /* : "null" */) + "[" + getStartOffset() + " .. " + getEndOffset() + "]";
  }
}
