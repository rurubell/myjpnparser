#!/usr/bin/perl

use strict;
use warnings;
use Scalar::Util qw( looks_like_number );


#Make random string
sub makeRandomString
{
	my $n_strlen = shift();
	my $s_alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	my $s_res = "";
	
	if( !looks_like_number( $n_strlen ) ) { die( (caller(0))[3] . " \$n_strlen = " . $n_strlen . " - not a number!\n" ); }
	if( int( $n_strlen ) <= 0 ) { die( (caller(0))[3] . " \$n_strlen = " . $n_strlen . " <= 0!\n" ); }
	
	for( my $i = 0; $i < $n_strlen; $i++ )
	{
		my $n_rand_pos = int( rand( length( $s_alpha ) ) );
		$s_res .= substr( $s_alpha, $n_rand_pos, 1 );
	}
	
	return $s_res;
}


1;
