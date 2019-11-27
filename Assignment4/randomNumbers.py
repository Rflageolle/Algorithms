#
#
#
#
#
#
#

import random, sympy
from datetime import datetime as dt
from math import gcd


def lcm(a, b):
    return a * b / gcd(a, b)

def getPrime(x):
    return sympy.nextprime(x)

def next_valid_prime(x):
    p = sympy.nextprime(x)
    while (p % 4 != 3):
        p = sympy.nextprime(p)
    return p

def newSeed():
    return dt.now().microsecond

class BBSRandom():
    def __init__(self):
        self.x = getPrime(3 * 10 ** 10)
        self.y = next_valid_prime(4 * 10 ** 10)
        self.n = 1000
        self.seed = newSeed()

    def getInt(self, n=10):
        return int(self.getBin(n)[:n], 2)

    def getBin(self, n=10):
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
