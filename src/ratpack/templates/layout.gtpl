yieldUnescaped '<!DOCTYPE html>'
html(lang:'en') {
    head {
        meta(charset:'utf-8')
        title(title ?: 'Kodemaker directory')
        meta('http-equiv': "Content-Type", content:"text/html; charset=utf-8")
        meta(name: 'viewport', content: 'width=device-width, initial-scale=1.0')

        script(src: '/js/jquery.min.js') {}
        script(src: '/js/bootstrap.min.js') {}
        link(href: '/css/bootstrap.min.css', rel: 'stylesheet')
        link(href: '/css/bootstrap-theme.min.css', rel: 'stylesheet')
        link(href: '/css/directory.css', rel: 'stylesheet')
        link(href: '/img/favicon.ico', rel: 'shortcut icon')
    }
    body {
        div(class:'container') {
            bodyContents()
        }
    }
}