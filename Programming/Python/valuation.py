#!/usr/bin/env python
# -*- encoding: utf-8 -*-
# valuation.py - class for representing classical propositional logic valuations

import re

class Valuation:
	"classical propositional logic valuations"

	def __init__(self):
		self.values = dict()		

	def setValue(self,atomic_formula, value):
		self.values[atomic_formula]=value
	
	def getValue(self,formula):
		if self.isCompositeFormula(formula):
			return self.getValueOfACompositeFormula(formula)
		elif formula in self.values:
			return self.values[formula]
		else:
			print "RAISE EXCEPTION"

	def getValueOfACompositeFormula(self,formula):
		formula = formula.replace("->","*")
		if formula.startswith("!"):
			return 1-self.values[formula[1:]]
		elif formula.startswith("("):
#			match = re.match(r"\((\w)[&|*](\w)\)", formula.replace("->","*"))
#			return self.values[match.group(1)] and self.values[match.group(1)] 
			formulaList = self.parse(formula)
#			if not (self.isCompositeFormula(formulaList[1]) or \
#				self.isCompositeFormula(formulaList[2])):
			if formulaList[0] == '&':
				return self.getValue(formulaList[1]) and self.getValue(formulaList[2])
			elif formulaList[0] == '|':
				return self.getValue(formulaList[1]) or self.getValue(formulaList[2])
			elif formulaList[0] == '*':
				return not self.getValue(formulaList[1]) or self.getValue(formulaList[2])

			

	def isCompositeFormula(self,formula):
		return formula.startswith("!") or formula.startswith("(")

	def parse(self,formula):
		"faz o parse de uma fórmula bem-formada"
		par_aberto=0
		j=0
		for i in formula:
			if i == "(":
				par_aberto+=1
			elif i==")":
				par_aberto-=1
			if par_aberto==1 and formula[j] in ['&','|','*']:
				return [formula[j], formula[1:j],  formula[j+1:len(formula)-1]]
			j+=1

		
