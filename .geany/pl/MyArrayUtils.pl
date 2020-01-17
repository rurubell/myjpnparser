#!/usr/bin/perl

use strict;
use warnings;
use List::MoreUtils qw( uniq );


#Print all array values
sub printArr
{
	my @s_arr = @{ shift() };
	chomp( @s_arr );
	
	for( my $i = 0; $i < @s_arr; $i++ )
	{
		print( $s_arr[ $i ] . "\n" );
	}
}


#Is string exists in Array
#return array position or -1
sub isArrayContainsString
{
	my @s_arr = @{ shift() };
	my $s_str = shift();
	
	for( my $i = 0; $i < @s_arr; $i++ )
	{
		if( index( $s_arr[ $i ], $s_str ) != -1 ) { return $i; }
	}
	
	return -1;
}


#Get only lines that contains string
sub getOnlyLinesThatContainsString
{
	my @s_src = @{ shift() };
	my $s_string = shift();
	my @s_res;
	
	for( my $i = 0; $i < @s_src; $i++ )
	{
		if( index( $s_src[ $i ], $s_string ) != -1 ) { push( @s_res, $s_src[ $i ] ); }
	}
	
	return @s_res;
}


#Get strings, that exists in first array (A), but not exist in second (B)
sub arrDifference
{
	my @s_A_arr = @{ shift() };
	my @s_B_arr = @{ shift() };
	my @s_res;
	
	for( my $i = 0; $i < @s_A_arr; $i++ )
	{
		my $n_flag = 0;
		for( my $j = 0; $j < @s_B_arr; $j++ )
		{
			if( index( $s_A_arr[ $i ], $s_B_arr[ $j ] ) != -1 ) { $n_flag = 1; }
		}
		
		if( $n_flag == 0 ) { push( @s_res, $s_A_arr[ $i ] ); }
	}
	
	return uniq( @s_res );
}


#Is string exists in Array, search begins from $n_position
#return array position or -1
sub getStringIndexFromArray
{
	my @s_array = @{ shift() };
	my $s_string = shift();
	my $n_position = shift();
	
	if( length( $s_string ) == 0 ) { die( (caller(0))[3] . " \$s_string length = 0!\n" ); }
	if( !looks_like_number( $n_position ) ) { die( (caller(0))[3] . " \$n_position = " . $n_position . " - not a number!\n" ); }
	if( $n_position > @s_array ) { die( (caller(0))[3] . " \$n_position = " . $n_position . " > \@s_array = " . @s_array . " !\n" ); }
	if( $n_position < 0 ) { die( (caller(0))[3] . " \$n_position < 0 !\n" ); }
	
	for( my $i = $n_position; $i < @s_array; $i++ )
	{
		if( index( $s_array[ $i ], $s_string ) != -1 ) { return $i; }
	}
	
	return -1;
}


1;
