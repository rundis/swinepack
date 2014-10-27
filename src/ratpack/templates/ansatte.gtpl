
layout 'layout.gtpl',
    title: title,
    bodyContents: contents {
        h1(title)

        table(class: "table table-striped table-bordered") {
            thead {
                tr {
                    th('Fornavn')
                    th('Etternavn')
                    th('Født')
                    th('')
                }
            }
            tbody { rows.each { ansatt ->
                    tr {
                        td(ansatt.fornavn)
                        td(ansatt.etternavn)
                        td(format.date(ansatt.fodt))
                        td {
                            a(href: "/ansatte/${ansatt.id}", class:"btn btn-default", 'Endre')
                        }
                    }
            } }
        }
    }
