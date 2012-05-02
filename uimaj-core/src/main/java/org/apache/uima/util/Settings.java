package org.apache.uima.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.uima.resource.ResourceConfigurationException;

/**
 * A <code>Settings</code> object holds the properties used for external parameter overrides.
 * 
 * Similar to java.util.Properties but: 
 *  - supports UTF-8 (so \\uXXXX escapes are not needed or supported)
 *  - keys must be valid Java identifiers (actually must not contain '=' ':' '}' or white-space)
 *  - reverses priority in that duplicate entries are ignored, i.e. once set values cannot be changed
 *  - multiple files can be loaded
 *  - values can contain references to other values, e.g. name = .... ${key} ....
 *  - arrays are represented as strings, e.g. '[elem1,elem2]', and can span multiple lines
 *  - '\' can be used in values to escape '$' '{' '[' ',' ']' 
 *   
 * @author burn
 * 
 */

public interface Settings {
  
  /**
   * Load properties from an input stream.  
   * Existing properties are not changed and a warning is logged if the new value is different.
   * May be called multiple times, so effective search is in load order.
   * Arrays are enclosed in [] and the elements may be separated by <code>,</code> or new-line, so 
   *   can span multiple lines without using a final \ 
   * 
   * @param in - Stream holding properties
   * @throws IOException
   */
  public void load(InputStream in) throws IOException;

  /**
   * Look up the value for a property.
   * Perform one substitution pass on ${key} substrings replacing them with the value for key.
   * Recursively evaluate the value to be substituted.  NOTE: infinite loops not detected!
   * If the key variable has not been defined, an exception is thrown.
   * To avoid evaluation and get ${key} in the output escape the $ or {
   * Arrays are returned as a comma-separated string, e.g. "[elem1,elem2]" 
   * Note: escape characters are not removed as they may affect array separators. 
   * 
   * @param name - name to look up
   * @return     - value of property
   * @throws ResourceConfigurationException
   */
  public String lookUp(String name) throws ResourceConfigurationException;

  /**
   * Return a set of keys of all properties loaded
   * 
   * @return - set of strings
   */
  public Set<String> getKeys();
}