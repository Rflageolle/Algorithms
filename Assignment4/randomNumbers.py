#
#
#
#
#
#
#

import random, sympy, re, sys
from datetime import datetime as dt
from math import gcd


# My RNG is based off of the Blum Blum Shub algorithm
#          2
# x    = x   mod M
#  n+1     n

# where M = pq such that p and q are large primes
i = 1

def lcm(a, b):
    return a * b / gcd(a, b)

def next_valid_prime(x):
    p = sympy.nextprime(x)
    while (p % 4 != 3):
        p = sympy.nextprime(p)
    return p

class BBSRandom():
    def __init__(self):
        self.x = 3 * 10 ** 10
        self.y = 4 * 10 ** 10
        self.n = 1000

    def newSeed(self):
        return dt.now().microsecond

    def getInt(self):
        p = next_valid_prime(self.x)
        q = next_valid_prime(self.y)
        m = p * q

        bits = ""
        x = self.newSeed()
        print(x)

        for _ in range(self.n):
            x = x * x % m
            b = x % 2
            bits += str(b)

        print(bits[0:20])
