package br.com.senai.ianesbank_master.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.MariaDBDialect;

public class MariaDb104Dialect extends MariaDBDialect {
 public MariaDb104Dialect() {
   super(DatabaseVersion.make(10, 4));
 }

 @Override
 protected DatabaseVersion getMinimumSupportedVersion() {
   return DatabaseVersion.make(10, 4);
 }
}
