# Assignment 4: Random Numbers
# Author: Ryan Flageolle
# Description: Analyse 2 random number generators, one hand written and on from
#              a library

import random, sympy
from datetime import datetime as dt
from math import gcd


def lcm(a, b):
    return a * b / gcd(a, b)

def getPrime(x):
    return sympy.nextprime(x)

# returns the next prime which is congruent to 3 % 4
def next_valid_prime(x):
    p = sympy.nextprime(x)
    while (p % 4 != 3):
        p = sympy.nextprime(p)
    return p

def newSeed():
    return dt.now().microsecond

class BBSRandom():
    """ Random Number Generator based on the Blum Blum Shub algorithm.

    Attributes
    ----------
    x : int
        Stored state of p multiplier.
    y : type
        Stored state of q multiplier.
    n : type
        number of steps before a number is generated.
    seed : type
        Seed for random number generator.

    """
    def __init__(self):
        self.x = getPrime(3 * 10 ** 10)
        self.y = next_valid_prime(4 * 10 ** 10)
        self.n = 10
        self.seed = newSeed()

    # returns integer value of random binary string
    def getInt(self, n=10):
        return int(self.getBin(n)[:n], 2)

    def getBin(self, n=10):
        """ returns a random binary string n bits long.

        Parameters
        ----------
        n : int
            Determines the number of bits returned by getBin().

        Returns
        -------
        String
            returns a random binary string

        """
        p = getPrime(self.x)
        q = next_valid_prime(self.y)
        m = p * q

        bits = ""
        x = self.seed

        for _ in range(self.n):
            x = x * x % m
            b = x % 2
            bits += str(b)

        self.seed = x % q
        self.x = q
        self.y = p
        return bits[:n]
