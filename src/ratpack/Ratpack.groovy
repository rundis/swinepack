import groovy.sql.GroovyRowResult
import no.kodemaker.directory.AnsattModule
import no.kodemaker.directory.DBCommands
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.form.Form
import ratpack.groovy.markuptemplates.MarkupTemplate
import ratpack.groovy.markuptemplates.MarkupTemplatingModule
import ratpack.groovy.sql.SqlModule
import ratpack.hikari.HikariModule

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack



final Logger log = LoggerFactory.getLogger(Ratpack.class)

MarkupTemplate template(Map<String, ?> model, String id) {
    Map formatters = [
        date: {LocalDate date -> date?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) }
    ]
    groovyMarkupTemplate(model + [format: formatters], id)
}

ratpack {
    bindings {
        add new HikariModule([URL: "jdbc:h2:mem:dev;INIT=CREATE SCHEMA IF NOT EXISTS DEV"], "org.h2.jdbcx.JdbcDataSource")
        add new SqlModule()
        add new MarkupTemplatingModule()
        add new AnsattModule()

        init { DBCommands dbCommands ->
            log.info("Initializing...")
            dbCommands.createTables()
            log.info("Done initializing...")
        }
    }

    handlers {DBCommands dbCommands ->

        get("ansatte") {
            dbCommands.listAll().toList().subscribe { List<Map> rows ->
                render template("ansatte.gtpl", rows: rows, title: "Ansatte")
            }
        }

        handler("ansatte/create") {
            byMethod  {
                get {
                    render template("ansattform.gtpl", title: "Ny", ansatt: [:])
                }
                post {
                    Form form = parse(Form)
                    def ansatt = [
                        fornavn: form["fornavn"],
                        etternavn: form["etternavn"],
                        fodt: LocalDate.parse(form.fodt, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    ]

                    dbCommands.nyAnsatt(ansatt).single().subscribe {Long id ->
                        redirect "/ansatte/$id"
                    }

                }
            }
        }

        handler("ansatte/:id") {
            byMethod {
                get{
                    dbCommands.findById(pathTokens["id"].toLong()).single().subscribe {Map ansatt ->
                        render template("ansattform.gtpl", title: "Endre", ansatt: ansatt)
                    }
                }
                post {
                    Form form = parse(Form)

                    def ansatt = [
                        id: form.id.toLong(),
                        fornavn: form["fornavn"],
                        etternavn: form["etternavn"],
                        fodt: LocalDate.parse(form.fodt, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    ]
                    log.info ansatt.toString()

                    dbCommands.oppdaterAnsatt(ansatt).subscribe {
                        redirect "/ansatte/$ansatt.id"
                    }
                }
            }
        }

        assets "public"
    }
}
