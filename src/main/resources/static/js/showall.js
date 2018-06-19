$(document).ready(function(){
	$('#showbio').click(function(){
		if($('#showbio').hasClass('showall')){
			$('#bio').removeClass('line-clamp').addClass('line-clamp-shows');
			$('#showbio').text("Show Less");
			$('#showbio').addClass('showless').removeClass('showall');
		}
		else if($('#showbio').hasClass('showless')){	
			$('#bio').removeClass('line-clamp-shows').addClass('line-clamp');
			$('#showbio').text("Show All")
			$('#showbio').addClass('showall').removeClass('showless');
		}
	});
	$('#showmoviescast').click(function(){
		if($('#showmoviescast').hasClass('showall')){
			$('#moviescast').removeClass('line-clamp').addClass('line-clamp-shows');
			$('#showmoviescast').text("Show Less");
			$('#showmoviescast').addClass('showless').removeClass('showall');
		}
		else if($('#showmoviescast').hasClass('showless')){	
			$('#moviescast').removeClass('line-clamp-shows').addClass('line-clamp');
			$('#showmoviescast').text("Show All")
			$('#showmoviescast').addClass('showall').removeClass('showless');
		}
	});
	$('#showmovieswritten').click(function(){
		if($('#showmovieswritten').hasClass('showall')){
			$('#movieswritten').removeClass('line-clamp').addClass('line-clamp-shows');
			$('#showmovieswritten').text("Show Less");
			$('#showmovieswritten').addClass('showless').removeClass('showall');
		}
		else if($('#showmovieswritten').hasClass('showless')){	
			$('#movieswritten').removeClass('line-clamp-shows').addClass('line-clamp');
			$('#showmovieswritten').text("Show All")
			$('#showmovieswritten').addClass('showall').removeClass('showless');
		}
	});
	$('#showmoviesdirected').click(function(){
		if($('#showmoviesdirected').hasClass('showall')){
			$('#moviesdirected').removeClass('line-clamp').addClass('line-clamp-shows');
			$('#showmoviesdirected').text("Show Less");
			$('#showmoviesdirected').addClass('showless').removeClass('showall');
		}
		else if($('#showmoviesdirected').hasClass('showless')){	
			$('#moviesdirected').removeClass('line-clamp-shows').addClass('line-clamp');
			$('#showmoviesdirected').text("Show All")
			$('#showmoviesdirected').addClass('showall').removeClass('showless');
		}
	});
	$('#showtv').click(function(){
		if($('#showtv').hasClass('showall')){
			$('#tv').removeClass('line-clamp').addClass('line-clamp-shows');
			$('#showtv').text("Show Less");
			$('#showtv').addClass('showless').removeClass('showall');
		}
		else if($('#showtv').hasClass('showless')){	
			$('#tv').removeClass('line-clamp-shows').addClass('line-clamp');
			$('#showtv').text("Show All")
			$('#showtv').addClass('showall').removeClass('showless');
		}
	});
	$('#showtvcreated').click(function(){
		if($('#showtvcreated').hasClass('showall')){
			$('#tvcreated').removeClass('line-clamp').addClass('line-clamp-shows');
			$('#showtvcreated').text("Show Less");
			$('#showtvcreated').addClass('showless').removeClass('showall');
		}
		else if($('#showtvcreated').hasClass('showless')){	
			$('#tvcreated').removeClass('line-clamp-shows').addClass('line-clamp');
			$('#showtvcreated').text("Show All")
			$('#showtvcreated').addClass('showall').removeClass('showless');
		}
	});
	$('#showtvcast').click(function(){
		if($('#showtvcast').hasClass('showall')){
			$('#tvcast').removeClass('line-clamp').addClass('line-clamp-shows');
			$('#showtvcast').text("Show Less");
			$('#showtvcast').addClass('showless').removeClass('showall');
		}
		else if($('#showtvcast').hasClass('showless')){	
			$('#tvcast').removeClass('line-clamp-shows').addClass('line-clamp');
			$('#showtvcast').text("Show All")
			$('#showtvcast').addClass('showall').removeClass('showless');
		}
	});
});

