package main

import (
	"bytes"
	"context"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	v4 "github.com/aws/aws-sdk-go-v2/aws/signer/v4"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/credentials/stscreds"
	"github.com/aws/aws-sdk-go-v2/service/sts"
)

/*
*
Steps
 1. Login to aws with main creds
 2. STS: Assume role and generate ephimeral creds based on main creds
 3. Prepare http graphql request
 4. Sign http graphql request with aws signer v4
 5. Make request
*/
func main() {

	// 1. Login to aws with main creds
	// Define aws client with default config
	cfg, err := config.LoadDefaultConfig()
	if err != nil {
		panic("configuration error, " + err.Error())
	}

	// 2. STS: Assume role and generate ephimeral creds based on main creds
	// Create aws sts client from default config
	client := sts.NewFromConfig(cfg)

	// Get sts credentials by assuming role
	creds := stscreds.NewAssumeRoleProvider(client, "arn:role")
	stsCreds, _ := creds.Retrieve(context.Background())

	// 3. Prepare http graphql request
	// Define graphql payload
	graphqlPayload := map[string]string{
		"query":     `{}`,
		"variables": `{}`,
	}

	// Create http client
	httpclient := &http.Client{Timeout: time.Second * 10}

	// Prepare requests
	payload, _ := json.Marshal(graphqlPayload)
	request, err := http.NewRequest("POST", "https://app.domain/uri", bytes.NewBuffer(payload))

	// Set headers
	request.Header.Set("x-apigw-api-id", "api-id") // If api gateway used
	request.Header.Set("Content-Type", "application/json")

	// Create payload hash
	hash := sha256.New()
	hash.Reset()
	hash.Write([]byte(payload))
	payloadHash := hex.EncodeToString(hash.Sum(nil))

	// 4. Sign http graphql request with aws signer v4
	// Sign http request with aws signer v4
	signer := v4.NewSigner()
	signer.SignHTTP(context.Background(), stsCreds, request, payloadHash, "execute-api", "region", time.Now())

	// 5. Make request
	// Trigger http request
	response, err := httpclient.Do(request)
	if err != nil {
		fmt.Printf("The HTTP request failed with error %s\n", err)
	}
	defer response.Body.Close()

	// Print response
	data, _ := io.ReadAll(response.Body)
	fmt.Println(string(data))
}
