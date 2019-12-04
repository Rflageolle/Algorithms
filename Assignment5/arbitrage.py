
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

    graph={}

    def addVertex(self, vertex):
        if vertex not in self.graph:
            self.graph[vertex] = []

    def addExchange(self, edge1, edge2, weight):
        self.addWeightedEdge(edge1, edge2, float(weight))
        self.addWeightedEdge(edge2, edge1, reciprical(float(weight)))

    def addWeightedEdge(self, edge1, edge2, weight):
        if edge1 not in self.graph:
            self.graph[edge1]=[(edge2, weight)]
        else:
            self.graph[edge1].append((edge2, weight))

    def show_edges(self):
        for node in self.graph:
            currency = node + ' -> '
            for item in self.graph[node]:
                next, weight = item
                currency += '(' + next + ' ' + str(weight) + ')'
            print(currency)

    def check_paths(self, node):
        largest = 0.0
        n = None
        for item in self.graph[node]:
            next, weight = item
            if float(weight) > largest:
                largest = float(weight)
                n = next

        return (n, largest)

    #
    #
    #
    def largest_path(self, node):
        path = []
        next = node
        if node not in self.graph:
            print("Oddly enough this doesnt work")
        else:
            while True:
                next, weight = self.check_paths(next)
                if next == node:
                    break
                else:
                    path.append((next, weight))

        return path

    def DFSUtil(self, v, visited):

        # Mark the current node as visited and print it
        visited[v]= True
        print v,

        # Recur for all the vertices adjacent to
        # this vertex
        for i in self.graph[v]:
            if visited[i] == False:
                self.DFSUtil(i, visited)


    # The function to do DFS traversal. It uses
    # recursive DFSUtil()
    def DFS(self):
        V = len(self.graph)  #total vertices

        # Mark all the vertices as not visited
        visited=[False]*(V)

        # Call the recursive helper function to print
        # DFS traversal starting from all vertices one
        # by one
        for i in range(V):
            if visited[i] == False:
                self.DFSUtil(i, visited)

def reciprical(weight):
    return round(1.0 / weight, 3)

def read_in_graph():
    g = Graph()
    with open('exchangeRates.txt') as f:
        for num, str in enumerate(f):
            if num > 5:
                start, stop, weight = str.split()
                g.addExchange(start, stop, weight)

    g.show_edges()
    return g

g = read_in_graph()
node, weight = g.check_paths('Canada')
print(node, weight)
print(len(g))
