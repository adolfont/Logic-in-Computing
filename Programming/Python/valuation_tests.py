#!/usr/bin/env python
# -*- encoding: utf-8 -*-
# valuation_tests.py - tests for the class that represents classical propositional logic valuations

from valuation import *
import unittest

class TestValuation(unittest.TestCase):
	def setUp(self):
		self.valuation = Valuation()
		self.valuation.setValue("p",1)
		self.valuation.setValue("q",1)
		self.valuation.setValue("r",0)
		self.valuation.setValue("s",0)


	def test_valuation_of_atomic_formula_works(self):
		self.assertEquals(1, self.valuation.getValue("p"))
		self.assertEquals(1, self.valuation.getValue("q"))
		self.assertEquals(0, self.valuation.getValue("r"))


	def test_valuation_of_negation_of_an_atomic_formula_works(self):
		self.assertEquals(0, self.valuation.getValue("!p"))

	def test_valuation_of_and_composite_formula_work(self):
		self.assertEquals(1, self.valuation.getValue("(p&q)"))
		self.assertEquals(0, self.valuation.getValue("(r&q)"))

	def test_valuation_of_or_composite_formula_work(self):
		self.assertEquals(1, self.valuation.getValue("(p|q)"))
		self.assertEquals(1, self.valuation.getValue("(r|q)"))
		self.assertEquals(0, self.valuation.getValue("(r|s)"))

	def test_valuation_of_implies_composite_formula_work(self):
		self.assertEquals(1, self.valuation.getValue("(p->q)"))
		self.assertEquals(1, self.valuation.getValue("(r->q)"))
		self.assertEquals(0, self.valuation.getValue("(p->s)"))

	def test_valuation_of_and_and_composite_formula_work(self):
		self.assertEquals(0, self.valuation.getValue("(p&(q&r))"))
		self.assertEquals(1, self.valuation.getValue("(p&(q&!r))"))


if __name__ == "__main__":
	unittest.main()

