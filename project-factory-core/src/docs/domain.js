$(document).ready(function(){
    var domainName = window.location.hostname;
    if(domainName){
        $('.hljs-string').each(function () {
            $(this).html( $(this).html().replace(/https:\/\/dev.revature.com\/api/g,getServerURL()));
            $(this).html( $(this).html().replace(/\/api\//g,"/"));
        });
        var els = document.getElementsByClassName("hljs-attribute");
        Array.prototype.forEach.call(els, function(el) {
            if(el.innerHTML === "Host")
                el.nextSibling.nodeValue = el.nextSibling.nodeValue.replace(/dev.revature.com/g,getDomainName());
        });
    }
});

function getServerURL() {
    return 'http://localhost:8087';
}

function getDomainName() {
    return 'localhost:8087';
}
