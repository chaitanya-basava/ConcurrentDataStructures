project_dir="$1"
num_runs="$2"
ds_num="$3"
w="$4"
key_space="$5"

for ((  n = 2;  n < 13;  n++ ));
do
  bash launcher.sh "$project_dir" "$num_runs" "$ds_num" "$n" "$w" "$key_space"
  echo "completed tests for num_threads = $n"
done
