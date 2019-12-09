from itertools import permutations, combinations
#
# {'edge': {('edge', weight), ('edge', weight)}
#
# class Graph:
#     graph = {}
#
#     def addEdge(self, start, stop, weight):
#         if start not in self.graph:
#             self.graph[start]=(stop, float(weight))
#         else:


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


g = read_in_graph()
currencies = set(g.graph.keys())
for size in range(3, len(currencies)+1):
    for subset in combinations(currencies, size):
        for path in permutations(subset):
            edges = list(zip(path, path[1:] + path[:1]))
            value = 1000.0
            for start, end in edges:
                value *= g.graph[start][end]
            if value > 1000.0:
                print(round(value, 2), *[frm for frm, _ in edges], sep='->')
