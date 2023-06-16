terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
    }
  }
}

provider "google" {
  credentials = file("/Users/limyshkalapala/docker-1/Terraform/csci-5409-cloud-389821-576de4354b9e.json")
  project     = "csci-5409-cloud-389821"
  region      = "us-central1"
}

provider "kubernetes" {
  config_path = "/Users/limyshkalapala/.kube/config"
}

resource "google_container_cluster" "my_cluster" {
  name               = "my-cluster"
  location           = "us-central1-c"
  initial_node_count = 1

  node_config {
    machine_type = "e2-medium"
    disk_size_gb = 100
    preemptible  = false
  }
}

resource "google_compute_disk" "my_disk" {
  name  = "my-disk"
  type  = "pd-standard"
  size  = 1
  zone  = "us-central1-c"
}
