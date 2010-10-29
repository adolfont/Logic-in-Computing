#!/usr/bin/env python
# -*- encoding: utf-8 -*-
# formula_tests.py - tests for the class that represents classical propositional logic formulas

from formula import *
from connective import *
import unittest

class TestFormula(unittest.TestCase):
	def test_build_atomic_formula(self):
		f = Formula(atom=0)
		self.assertEquals([], f.proper_subformulas())
		self.assertEquals("0", f.asString())
		self.assertEquals(None, f.connective)
	def test_build_negation_of_a_formula(self):
		f1 = Formula(atom=0)
		neg = Connective("!")
		neg_f1 = Formula(connective=neg,subformulas=[f1])
		self.assertEquals([f1], neg_f1.proper_subformulas())
		self.assertEquals("!0", neg_f1.asString())
	def test_build_binary_formula(self):
		f1 = Formula(atom=0)
		f2 = Formula(atom=1)
		andConnective = Connective("&")
		f1Andf2 = Formula(connective=andConnective, subformulas=[f1, f2])
		self.assertEquals([f1,f2], f1Andf2.proper_subformulas())
		self.assertEquals("(0&1)", f1Andf2.asString())


if __name__ == "__main__":
	unittest.main()

