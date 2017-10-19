/*BEGIN_COPYRIGHT_BLOCK
 *
 * Copyright (c) 2001-2017, JavaPLT group at Rice University (drjava@rice.edu).  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the 
 * following conditions are met:
 *    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *      disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the 
 *      following disclaimer in the documentation and/or other materials provided with the distribution.
 *    * Neither the names of DrJava, DrScala, the JavaPLT group, Rice University, nor the names of its contributors may 
 *      be used to endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software is Open Source Initiative approved Open Source Software.
 * Open Source Initative Approved is a trademark of the Open Source Initiative.
 * 
 * This file is part of DrScala.  Download the current version of this project from http://www.drscala.org/.
 * 
 * END_COPYRIGHT_BLOCK*/

package edu.rice.cs.drjava.model.definitions;

import java.io.IOException;
import java.io.StringReader;

import edu.rice.cs.util.Log;
import edu.rice.cs.util.swing.Utilities;

/** The simple lexer class for extracting package names from Scala files. This name may be divided across 
  * cumulative package statements as is legal in Scala.  If code for multiple packages is included in the
  * document, the returned value will be garbled.*/
public class PackageLexer extends java.io.StreamTokenizer {
  
  public static Log _log = new Log("GlobalModel.txt", false);
  
  /* inherited fields:
   *   String sval    // WARNING: sval is NOT interned so == comparison on sval DOES NOT WORK
   *   ...
   */
  
  String _text;  // Used in debugging code
  
  public PackageLexer(String text) {
    super(new StringReader(text));  // pass a Reader wrapping the text to the StreamTokenizer constructor
    _text = text;
    
    // Cancel the default single character comment status of '/'
    ordinaryChar('/');
    
    // accept Java/Scala style comments
    slashSlashComments(true);
    slashStarComments(true);
    
    // add '_', '.', and '$' to set of wordChars (package name is read as single word)
    wordChars('_', '_');
    wordChars('.', '.');
    wordChars('$', '$');
    
    // treat ';' as whitespace
    whitespaceChars(';', ';');
  }
  
  public String getPackageName() {
    
    _log.log("getPackageName() called on " + _text);
    
    StringBuilder name = new StringBuilder();
    String dot = "";  // dot is bound to the empty string only for the first iteration
    
    try {

      /* Concatenate the <name> tokens from the first token sequence of the form { package <name>}* using dot as the 
       * separator. Note that dot is the empty string on the first iteration and that period (dot) is a legal character
       * in a TT_WORD (identifier). */
      
      while (true) {
        
        /* Get the first token of the remaining text. */
        int token = nextToken();
        _log.log("token = " + token);
     
        
        /* Skip all tokens until the package keyword or EOF is found, perhaps skipping over import statements */
        while ((token != TT_EOF) && (token != TT_WORD || ! sval.equals("package"))) token = nextToken();
               
        if (token == TT_EOF) break;
        else { /* token is the package keyword */ 
        
          /* Get the token following the package keyword */
          token = nextToken();
               
          /* Break if an improperly formed package name is encountered */
          if (token != TT_WORD || sval.startsWith(".") || sval.endsWith(".") || sval.contains("..")) break;
               
          name.append(dot).append(sval);
          dot = ".";  // the separator character is "."
        }
      }
    }
    catch(IOException e) { 
      /* An I/O error aborts the loop */
    }
      
    String result = name.toString();
    _log.log("Given\n" + _text + "\nPackageLexer.getPackageName() returned '" + result + "'");
    return result;
  }
}