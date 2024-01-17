package net.fze.ext.hibernate;

import org.hibernate.cfg.Configuration;

public interface IConfigurationProvider {
    void apply(Configuration configuration);
}
