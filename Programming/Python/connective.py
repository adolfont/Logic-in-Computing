#!/usr/bin/env python
# -*- coding: utf-8 -*-
# connective.py - class for representing classical propositional logic connectives

class Connective:
   
	def __init__(self,symbol):
		self.symbol=symbol
	def __str__(self):
		return str(self.symbol)
		
if __name__ == "__main__":
	c = Connective("x")
	print "Executando módulo..."
	print c

