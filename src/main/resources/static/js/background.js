    $(function(){
        var x = 0;
        setInterval(function(){
            x+=0.1;
            $('body').css('background-position', x + 'px 0');
        }, 10);
    })

