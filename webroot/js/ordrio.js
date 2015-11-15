$(function () {
	var wsPort = window.location.port === '' ? '' : ':' + window.location.port;
	var wsUrl = window.location.protocol + '//' + window.location.hostname + wsPort;
	var sock = new SockJS(wsUrl+'/socket');

	$('#create-ordr-form').submit(function(e) {
		e.preventDefault();
		var formData = $(e.target).serialize();
		$.post('/api/ordr', formData)
		.success(function(data) {
			window.location.href = '/o/'+JSON.parse(data).id;
		});
	});

	if(window.location.pathname === '/') {
		$('#create-ordr').removeClass('hidden');
	}
	else if(stringStartsWith(window.location.pathname, '/o/')) {
		$('#ordr').removeClass('hidden');
	}

	sock.onopen = function() {
		console.log('open');
	};

	sock.onmessage = function(e) {
		var aggregate = JSON.parse(e.data);
	};

	sock.onclose = function() {
		console.log('close');
	};
});

function stringStartsWith (string, prefix) {
	return string.slice(0, prefix.length) == prefix;
}