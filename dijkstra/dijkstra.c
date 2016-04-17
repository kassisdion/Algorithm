/**
 * Algorithm homework #3
 *
 * "Dijkstra algorithm"
 * - on an undirected weighted graph with 200 vertices labeled 1 to 200
 * - run Dijkstra's shortest-path algorithm on this graph, using 1 (the first
 *   vertex) as the source vertex, and to compute the shortest-path distances
 *   between 1 and every other vertex of the graph. If there is no path between
 *   a * vertex v and vertex 1, we'll define the shortest-path distance between
 *   1 and v to be 1000000. 
 *
 * @ Mar. 7 2015
 * @ Jeongyeup Paek
 **/

/**
 * @ Student ID : 50151252
 * @ Name       : Faisant Florian
**/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
 #include <stdbool.h>
#ifdef _WIN64           // define something for Windows (64-bit)
    #include <time.h>
#elif _WIN32            // define something for Windows (32-bit)
    #include <time.h>
#elif __APPLE__         // apple
    #include <sys/time.h>
#elif __linux           // linux
    #include <time.h>
#elif __unix            // Unix
    #include <time.h>
#elif __posix           // POSIX
    #include <time.h>
#endif


#define MAX_DISTANCE    1000000 // if no path exists
#define MAX_NUM_NODES   200     // 200 vertices labeled 1 to 200
#define MAX_VERTEX_ID   200     // 200 vertices labeled 1 to 200

typedef struct edge_s {
    int             toid;
    int             w;      // weight
    struct edge_s   *next;
} edge_t;

typedef struct vertex_s {
    int             id;         // node id
    unsigned int    dist;       // minimum distance to source
    int             f;          // fromId
    edge_t          *edges;     // chained list of edge
    bool            is_current; // saving algorithm progression
} vertex_t;

vertex_t *m_varray[MAX_NUM_NODES]; // array of all vertices

int m_n = 0;    // total number of i->j edges. 
int m_e = 0;    // 'm_e/2' is the actual number of undirected edges


vertex_t    *
find_vertex(unsigned int id)
{
    if (id > MAX_VERTEX_ID) {
        printf("%s: error, invalid vertex id %d\n", __func__, id);
        return NULL;
    }

    //m_varray[id]->id = id+1 (see graph initialization in main)
    return m_varray[id - 1];
}

// find vertex and add if does not exist
vertex_t    *
add_vertex(unsigned int id)
{
    vertex_t *v;

    if (id > MAX_VERTEX_ID) {
        printf("%s: error, invalid vertex id %d\n", __func__, id);
        return NULL;
    }

    if ((v = find_vertex(id)) != NULL) {
        return v;
    }

    v = m_varray[id];
    v->id = id;
    v->dist = MAX_DISTANCE;
    v->f = 0;

    return v;
}

// delete all malloc'ed memory (edges and vertices)
void
delete_graph()
{
    edge_t *tmp;
    edge_t *head;

    int i;
    for (i = 0; i < MAX_NUM_NODES; i++) {
        head = m_varray[i]->edges;
        while (head != NULL) {
            tmp = head;
            head = head->next;
            free(tmp);
        }
        free(m_varray[i]);
    }
}

// print shortest distance of all nodes
void
print_all_shortest_distance()
{
    int i = 0;
    while (i < MAX_NUM_NODES) {
        printf("Node %d : distance=%d\n", m_varray[i]->id,
         m_varray[i]->dist);
        i++;
    }
}

// print shortest path of all nodes
void
print_all_shortest_path()
{
    //TODO
}

edge_t    *
find_edge(unsigned int fromid, unsigned int toid)
{
    vertex_t *v;
    if ((v = find_vertex(fromid)) == NULL) {
        return NULL;
    }

    edge_t *current = v->edges;
    while (current != NULL) {
        if (current->toid == toid) {
            return current;
        }
        current = current->next;
    }
    return NULL;
}

// find edge and add if does not exist
edge_t    *
add_edge(unsigned int fromid, unsigned int toid, unsigned int ecost)
{
    if (fromid > MAX_VERTEX_ID || toid > MAX_VERTEX_ID) {
        printf("%s: error, invalid vertex id %d->%d\n", __func__, fromid, toid);
        return NULL;
    }

    vertex_t *v;
    if ((v = find_vertex(fromid)) == NULL) {
        return NULL;
    }

    if (v->edges == NULL) {
        v->edges = (edge_t *) calloc(sizeof(edge_t), 1);
        v->edges->next = NULL;
        v->edges->toid = toid;
        v->edges->w = ecost;

        return v->edges;
    }
    else {
        /*
         * We could call find_edge(fromid, toid)
         *  But it will call find_vertex() and check result
         *  (2 useless operation)
         */
        edge_t *current = v->edges;
        while (current->next != NULL) {
            current = current->next;
        }

        current->next = (edge_t *) malloc(sizeof(edge_t));
        current->next->next = NULL;
        current->next->toid = toid;
        current->next->w = ecost;

        return current->next;
    }

    return NULL;
}

//find the vertex who has the minimum distance
vertex_t    *
find_min_vertex() {

    int i, min_i, min_dist;
    
    /*
     * 0 = starting point
     * So we init our first distance with i=1
     */
    i = min_i = 1;
    min_dist = m_varray[i]->dist;

    for (i = 2; i < MAX_NUM_NODES; i++) {
        if (m_varray[i]->dist < min_dist && m_varray[i]->is_current == false) {
            min_dist = m_varray[i]->dist;
            min_i = i;
        }
    }
    m_varray[min_i]->is_current = true;

    return m_varray[min_i];
}

// run Dijkstra algorithm
void
run_dijkstra()
{
    int start_point = 1;
    int index = start_point - 1;
    int i = 0;

    edge_t *edge = m_varray[index]->edges;
    m_varray[index]->f = 0;
    m_varray[index]->dist = 0;

    while (edge != NULL) {
        m_varray[(edge->toid) - 1]->dist = edge->w;
        m_varray[(edge->toid) - 1]->f = start_point;

        edge = edge->next;
    }

    for (i = 0; i < MAX_NUM_NODES; i++) {
        vertex_t *v = find_min_vertex();
        for (edge = v->edges; edge != NULL; edge = edge->next) {
            if (v->dist + edge->w < m_varray[edge->toid-1]->dist) {
                m_varray[(edge->toid) - 1]->dist = v->dist + edge->w;
                m_varray[(edge->toid) - 1]->f = v->id;
            }
        }
    }
}

int
read_input_file(char *filename)
{
    FILE *fp;
    char linebuf[1000];

    // clear global data before reading the file
    m_n = 0;

    // adjacency list representation of a simple undirected graph
    if ((fp = fopen(filename, "r")) == NULL) {
        perror("Error opening input file");
        return -1;
    }

    // read data file, line by line
    int i;
    int token_count;
    int token_array[1000] = {0};
    while (fgets(linebuf, sizeof(linebuf), fp) != NULL) {

        if (m_n > MAX_NUM_NODES) {
            printf("too many data (n = %d)\n", m_n);
            break;
        }

        //count element
        token_count = 0;
        for (i = 1; linebuf[i] != '\n' && linebuf[i] != '\0'; i++) {
            if (linebuf[i] == '\t' || linebuf[i] == ',' || linebuf[i] == ' ') {
                token_count++;
                token_array[token_count] = i + 1;
            }
        }

        for (i = 1; i < token_count; i += 2) {
            int fromid = m_n + 1;
            int toid = 0;
            int ecost = 0;

            sscanf(&linebuf[token_array[i]], "%d", &toid);
            sscanf(&linebuf[token_array[i + 1]], "%d", &ecost);

            if (add_edge(fromid, toid, ecost) == NULL) {
                return -1;
            }

            m_e++;
        }

        m_n++;
    }

    fclose(fp);

    printf("%s: total: %d nodes, %d edges\n", __func__, m_n, m_e / 2);

    return 0;
}


int
main(int argc, char *argv[])
{
    /*
    ** Check the number of argument
    */
    if (argc != 2) {
        printf("No input file\n");
        return 0;
    }

    /*
     * Allocate memory to our graph
     *   And then
     * Init the graph
     */
    int i = 0;
    while (i < MAX_NUM_NODES) {
        m_varray[i] = malloc(sizeof(vertex_t));
        m_varray[i]->id = i + 1;
        m_varray[i]->dist = MAX_DISTANCE;
        m_varray[i]->f = 0;
        m_varray[i]->edges = NULL;
        m_varray[i]->is_current = false;
        i++;
    }

    /*
     * Read input file from filename (argv[1)
     *   And
     *  Complete our graph
     */
    char *filename = argv[1];
    if (read_input_file(filename) == -1) {
        printf("Parsing error\n");
        return 0;
    }

    /*
     * Start the clock for running time calculation
     * run dijkstra here 
     */
    clock_t start = clock();

    run_dijkstra();

    /*
     * print shortest distance of all nodes in increasing order of ID 
     */
    print_all_shortest_distance();

    /*
     * if possible, print shortest path of all nodes 
     */
//    if (option) {
//        print_all_shortest_path();
//    }

    printf("homework output:\n");
    printf("shortest distance to vertices 7,37,59,82,99,115,133,165,188,197\n");
    printf("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
        m_varray[6]->dist,
        m_varray[36]->dist, m_varray[58]->dist, m_varray[81]->dist,
        m_varray[98]->dist, m_varray[114]->dist, m_varray[132]->dist,
        m_varray[164]->dist, m_varray[187]->dist, m_varray[196]->dist);

    /*
     * calculate running time 
     */
    clock_t stop = clock();
    double elapsed = (double)(stop - start) * 1000.0 / CLOCKS_PER_SEC;

    printf("running time = %f seconds\n", elapsed);

    /*
     * memory clean up 
     */
    delete_graph();

    return 0;
}
