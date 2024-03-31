project_dir="$1"
num_runs="$2"
ds_num="$3"
n="$4"
w="$5"
key_space="$6"

if [[ $project_dir == *".jar" ]]; then
  jar_path="$project_dir"
else
  mvn -f "$project_dir" clean package
  jar_path="$project_dir/target/ConcurrentDataStructures-1.0-SNAPSHOT-jar-with-dependencies.jar"
fi

mkdir "./tests/"

rm "./tests/$ds_num-$w-$key_space/$n.csv"

for ((  i = 0;  i < num_runs;  i++ )); do
  java -jar "$jar_path" utd.multicore.Main -d "$ds_num" -n "$n" -w "$w" -k "$key_space"
  echo "Run $i done"
  echo "---------------------------"
done

# bash launcher.sh "./" 10 1 10 70 100
