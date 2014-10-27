package no.kodemaker.directory

import com.google.inject.Inject
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import ratpack.exec.ExecControl

import java.time.LocalDate
import java.time.ZoneId

import static ratpack.rx.RxRatpack.observe
import static ratpack.rx.RxRatpack.observeEach


@Slf4j
class DBCommands {
    private final Sql sql
    private final ExecControl execControl

    @Inject
    public DBCommands(Sql sql, ExecControl execControl) {
        this.sql = sql
        this.execControl = execControl
    }

    void createTables() {
        sql.executeInsert "drop table if exists ansatt"
        sql.executeInsert """
          create table ansatt (
            id identity,
            fornavn varchar(100),
            etternavn varchar(100),
            fodt date
          )"""


        def date = {String d -> Date.parse("dd.MM.yyyy", d)}


        List ansatte = [
            [fornavn: "Magnus", etternavn: "Rundberget", fodt: date("10.10.1973")],
            [fornavn: "André", etternavn: "Bonkowski", fodt: date("09.11.1967")]
        ]

        ansatte.each {Map ansatt ->
            sql.executeInsert ansatt, "insert into ansatt(fornavn, etternavn, fodt) values(:fornavn, :etternavn, :fodt)"
        }
    }

    rx.Observable<Map> listAll() {
        observeEach(execControl.blocking {
            sql.rows("select * from ansatt").collect {toAnsatt(it)}
        })
    }

    rx.Observable<Map> findById(Long id) {
        observe(execControl.blocking {
            toAnsatt(
                sql.firstRow("select * from ansatt where id = :id", [id: id])
            )
        })
    }

    rx.Observable<Void> oppdaterAnsatt(Map ansatt) {
        observe(execControl.blocking {
            sql.executeUpdate(
                """
                    update ansatt
                    set fornavn = :fornavn, etternavn = :etternavn, fodt = :fodt
                    where id = :id
            """, ansattToDbParams(ansatt))
        })
    }


    rx.Observable<Long> nyAnsatt(Map ansatt) {
        observe(execControl.blocking {
            def res = sql.executeInsert(
                """
                    insert into ansatt (fornavn, etternavn, fodt)
                    values (:fornavn, :etternavn, :fodt)
            """, ansattToDbParams(ansatt))

            res[0][0]
        })
    }

    private Map toAnsatt(GroovyRowResult row) {
        [
            id:         row.id,
            fornavn:    row.fornavn,
            etternavn:  row.etternavn,
            fodt:       row.fodt.toLocalDate()
        ]
    }

    private Map ansattToDbParams(Map ansatt) {
        ansatt.subMap("id", "fornavn", "etternavn") + [fodt: java.sql.Date.valueOf(ansatt.fodt)]
    }




}
