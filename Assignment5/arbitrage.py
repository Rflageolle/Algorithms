# Author: Ryan Flageolle
# Assignment:  Program 5 - Arbitrage
# Description:
#  1.  Build a directed weighted graph that represents exchange rates. Each
#      node of the graph represents a currency and each directed weighted edge
#      represents the exchange rate the edge weight is what you multiply the
#      from-node by to get the corresponding amount of currency in the to-node.
#  2.  Find sequences of trades that will result in a profit. Print out the
#      trades and the resulting profit if $1000 is invested.

from itertools import permutations, combinations
from functools import cmp_to_key


class Graph:

    graph = {}

    def addVertex(self, vertex):
        if vertex not in self.graph:
            self.graph[vertex] = dict()

    def addExchange(self, edge1, edge2, weight):
        self.addWeightedEdge(edge1, edge2, float(weight))
        self.addWeightedEdge(edge2, edge1, reciprical(float(weight)))

    def addWeightedEdge(self, edge1, edge2, weight):
        if edge1 not in self.graph:
            self.graph[edge1] = dict([(edge2, weight)])
        else:
            self.graph[edge1][edge2] = weight

    def show_edges(self):
        for node, froms in self.graph.items():
            for to, weight in froms.items():
                print(node, '->', to, '=', round(weight, 3))


def reciprical(weight):
    return 1.0 / weight


def read_in_graph():
    with open('exchangeRates.txt') as f:
        f.readline()
        g = Graph()
        for line in f.readlines():
            start, stop, weight = line.rstrip("\n").split("\t")
            g.addExchange(start, stop, weight)
    return g


def arbitrage(g, start_investment):
    arbitrages = []
    currencies = set(g.graph.keys())
    for size in range(3, len(currencies)+1):
        for subset in combinations(currencies, size):
            for path in permutations(subset):
                edges = list(zip(path, currencies[1:] + path[:1]))
                value = float(start_investment)
                for start, end in edges:
                    value *= g.graph[start][end]
                if value > 1000.0:
                    arbitrages.append((round(value, 2),
                                      [frm for frm, _ in edges]))

    return arbitrages





def compare_arbitrages(item_1, item_2):
    value_1 = item_1[0]
    value_2 = item_2[0]

    if (value_1 < value_2):
        return -1
    elif (value_1 > value_2):
        return 1
    else:
        return 0


g = read_in_graph()
a = sorted(arbitrage(g, 1000), key=cmp_to_key(compare_arbitrages))

f = open('test2.txt', 'w')
s = 0
for v, e in a:
    if s < 100:
        p = round(v - 1000, 2)
        line = "profit: {}  total: {}     path: {}\n".format(p, v, e)
        f.write(line)
    else:
        break
    s += 1

f.close()
# for size in range(3, len(currencies)+1):
#     for subset in combinations(currencies, size):
#         for path in permutations(subset):
#             edges = list(zip(path, path[1:] + path[:1]))
#             value = 1000.0
#             for start, end in edges:
#                 value *= g.graph[start][end]
#             if value > 1000.0:
#                 print(round(value, 2), *[frm for frm, _ in edges], sep='->')
