#!/usr/bin/env python
# -*- coding: utf-8 -*-
# formula.py - class for representing classical propositional logic formulas

class Formula:
	"classical propositional logic formulas"
	def __init__(self,atom=None,connective=None,subformulas=[]):
		self.atom=atom
		self.connective=connective
		self.subformulas=subformulas
	def proper_subformulas(self):
		return self.subformulas		
	def connective(self):
		return self.connective
	def _get_connective(self):
		return self.connective
	def asString(self):
		if self.connective == None:
			return str(self.atom)
		elif self.connective.asString() == "!":
			return "%s%s" % (self.connective.asString(), self.subformulas[0].asString())
		elif self.connective.asString() in ["&", "|", "->"]:
			return "(%s%s%s)" % (self.subformulas[0].asString(), self.connective.asString(), self.subformulas[1].asString())
		else:
			pass

		
		
		
if __name__ == "__main__":
	f = Formula(atom=0)
	print "Executando módulo..."
	print f.proper_subformulas()
	print f.asString()
	print f.connective

