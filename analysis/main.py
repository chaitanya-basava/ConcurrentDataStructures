import glob
import pandas as pd
import matplotlib.pyplot as plt
from collections import defaultdict

if __name__ == "__main__":
    ds = 2

    files = glob.glob(f"../tests/{ds}*/*.csv")

    WRITE_DIST = "write distribution"
    KEY_SPACE = "key space"

    id_algo = {
        1: {
            "ds": "Linked List",
            "10-100": {
                WRITE_DIST: 10,
                KEY_SPACE: 100,
            },
            "10-10000": {
                WRITE_DIST: 10,
                KEY_SPACE: 10000,
            },
            "100-100": {
                WRITE_DIST: 100,
                KEY_SPACE: 100,
            },
            "100-10000": {
                WRITE_DIST: 100,
                KEY_SPACE: 10000,
            },
        },
        2: {"ds": "Stack", "0-0": "Stack"},
        3: {
            "ds": "Unbalanced BST",
            "10-100": {
                WRITE_DIST: 10,
                KEY_SPACE: 100,
            },
            "10-10000": {
                WRITE_DIST: 10,
                KEY_SPACE: 10000,
            },
            "100-100": {
                WRITE_DIST: 100,
                KEY_SPACE: 100,
            },
            "100-10000": {
                WRITE_DIST: 100,
                KEY_SPACE: 10000,
            },
        },
    }

    results = defaultdict(lambda: defaultdict(dict))

    for file in files:
        algo_num, write_dist, key_space = map(int, file.split("/")[-2].split("-"))
        num_threads = int(file.split("/")[-1].split(".")[0])
        data = pd.read_csv(file, header=None, names=["throughput", "avg_TAT"])

        averages = data.mean().tolist()

        results[algo_num][f"{write_dist}-{key_space}"][num_threads] = averages

    print(results)

    metrics = ["throughput", "avg_TAT"]
    for i, metric in enumerate(metrics):
        plt.figure(i)
        for algo in results:
            for run_type in results[algo]:
                threads = sorted(results[algo][run_type].keys())
                values = [results[algo][run_type][t][i] for t in threads]
                plt.plot(threads, values, label=f"{id_algo[algo][run_type]}")
        plt.xlabel("Number of Threads")
        plt.ylabel(metric)
        plt.title(f"{metric} vs Number of Threads")
        plt.legend()
        plt.grid(True)

        plt.savefig(f"./charts/{id_algo[ds]['ds']}_{metric}.png")
