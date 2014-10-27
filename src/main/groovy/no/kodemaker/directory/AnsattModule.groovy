package no.kodemaker.directory

import com.google.inject.AbstractModule
import com.google.inject.Scopes


class AnsattModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DBCommands.class).in(Scopes.SINGLETON)
    }
}
