def column = [class: 'col-sm-10']
def columnOffset = [class: 'col-sm-offset-2 col-sm-10']
def formGroup = [class: 'form-group']
def controlLabel(id) { [for: id, class: 'col-sm-2 control-label'] }
def inputText(id, value, opts=[disabled:false]) {
    def attr = [type: 'text', name: id, class: 'form-control', id: id, value: value]
    if (opts.disabled) { attr.disabled = 'disabled' }
    attr
}

layout 'layout.gtpl',
    title: title,
    bodyContents: contents {
        h1(title)

        form(class: "form-horizontal", role: "form", method: 'post', action: action) {
            input(type: 'hidden', name:'id', value: ansatt.id)

            div(formGroup) {
                label(controlLabel('fornavn'), 'Fornavn')
                div(column) {
                    input(inputText('fornavn', ansatt.fornavn)) {}
                }
            }

            div(formGroup) {
                label(controlLabel('etternavn'), 'Etternavn')
                div(column) {
                    input(inputText('etternavn', ansatt.etternavn)) {}
                }
            }

            div(formGroup) {
                label(controlLabel('fodt'), 'Født')
                div(column) {
                    input(inputText('fodt', format.date(ansatt.fodt))) {}
                }
            }

            div(formGroup) {
                div(columnOffset) {
                    button(type: "submit", class: "btn btn-primary", "Lagre")
                    a(href: "/ansatte", class: "btn btn-default", 'Back')
                }
            }
        }
    }