package com.brotherlogic.beer;

import java.io.IOException;
import java.util.Properties;

/**
 * Handles config for the project
 * 
 * @author simon
 * 
 */
public class Config
{
   public static String getValue(String key) throws IOException
   {
      Properties props = new Properties();
      props.load(Config.class.getResourceAsStream("cellar.config"));
      return props.getProperty(key);
   }
}
